package tn.esprit.spring.services.User;

import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.stereotype.Service;
import tn.esprit.spring.Exception.UserBlockedException;
import tn.esprit.spring.entities.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {


    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Integer> loginAttempts = new HashMap<>();
    private final Map<String, LocalDateTime> blockedUsers = new HashMap<>();
    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int BLOCK_DURATION_MINUTES = 1;

   /* public LoginAttemptService() {
        users.put("user1", new User());
        users.get("user1").setUserName("user1");
        users.get("user1").setUserPassword("password1");
    }*/
   public int getLoginAttempts(String userName) {
       Integer attempts = attemptsCache.get(userName);
       return attempts != null ? attempts : 0;
   }
    public void resetLoginAttempts(String userName) {
        attemptsCache.remove(userName);
    }
    public void incrementLoginAttempts(String userName) {
        int attempts = attemptsCache.getOrDefault(userName, 0);
        attemptsCache.put(userName, attempts + 1);
    }


    public boolean isUserBlocked(String username) {
        LocalDateTime blockedTime = blockedUsers.get(username);
        if (blockedTime == null) {
            return false;
        } else {
            LocalDateTime unblockTime = blockedTime.plusMinutes(BLOCK_DURATION_MINUTES);
            return LocalDateTime.now().isBefore(unblockTime);
        }
    }
    public void deBlockUser(String username){

           blockedUsers.remove(username);
    }

    public void blockUser(String username) {
        blockedUsers.put(username, LocalDateTime.now());
    }

/* public void login(String username, String password) throws UserBlockedException, InvalidCredentialsException {
        // Check if the user is blocked
        if (isUserBlocked(username)) {
            throw new UserBlockedException();
        }

        // Check if the user exists
        User storedUser = users.get(username);
        if (storedUser == null) {
            throw new InvalidCredentialsException();
        }

        // Check if the password is correct
        if (!storedUser.getUserPassword().equals(password)) {
            int loginAttempt = loginAttempts.getOrDefault(username, 0) + 1;
            loginAttempts.put(username, loginAttempt);

            // Block user if login attempts exceed limit
            if (loginAttempt >= MAX_LOGIN_ATTEMPTS) {
                blockUser(username);
                throw new UserBlockedException();
            } else {
                throw new InvalidCredentialsException();
            }
        } else {
            // Reset login attempts on successful login
            loginAttempts.remove(username);
        }
    }*/


}
