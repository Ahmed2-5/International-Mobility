package tn.esprit.spring.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.spring.Exception.UserBlockedException;
import tn.esprit.spring.entities.JwtRequest;
import tn.esprit.spring.entities.JwtResponse;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repositories.UserRepository;
import tn.esprit.spring.util.JwtUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginAttemptService loginAttemptService;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();

            authenticate(userName, userPassword);

        UserDetails userDetails = loadUserByUsername(userName);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userDao.findById(userName).get();
        if(user.getIsverified()==0){
            return new JwtResponse(null, "Please Verify your account first");
        }
        System.out.println(newGeneratedToken);
        return new JwtResponse(user, newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findById(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception {

        // Get the number of login attempts for the user
        int loginAttempts = loginAttemptService.getLoginAttempts(userName);

        // Check if the user is blocked
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
           throw new UserBlockedException();

        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
            loginAttemptService.resetLoginAttempts(userName);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            loginAttemptService.incrementLoginAttempts(userName);
            // If the user has exceeded the maximum number of login attempts, throw a UserBlockedException
            if (loginAttemptService.getLoginAttempts(userName) >= MAX_LOGIN_ATTEMPTS) {
                loginAttemptService.blockUser(userName);
                throw new UserBlockedException();

               /* if(loginAttemptService.isUserBlocked(userName)){

                }
                else {
                    loginAttemptService.deBlockUser(userName);
                }*/
            }

            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
