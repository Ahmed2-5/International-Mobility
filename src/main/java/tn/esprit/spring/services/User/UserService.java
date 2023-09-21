package tn.esprit.spring.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.Exception.UserNotFoundException;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.PasswordResetTokenRepository;
import tn.esprit.spring.repositories.RoleRepository;
import tn.esprit.spring.repositories.UserRepository;
import tn.esprit.spring.services.User.Twilio.SmsServiceImpl;
import tn.esprit.spring.util.UserCode;


import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userDao;

    @Autowired
    private RoleRepository roleDao;


    @Autowired
    private SmsServiceImpl smsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender userMailSender;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    public void initRoleAndUser() {

        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly created record");
        roleDao.save(userRole);

        Role partnerRole = new Role();
        partnerRole.setRoleName("Partner");
        partnerRole.setRoleDescription("Default role for newly created record");
        roleDao.save(partnerRole);


    }

    public User registerNewUser(User user) {
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        user.setIsverified(0);
        User saveduser=userDao.save(user);
        emailService.sendVerificationEmail(saveduser);
        return saveduser;
    }
    public User activateUser(String token) {
        User user = userDao.findByVerificationToken(token);
        if (user != null) {
            user.setIsverified(1);
            user.setVerificationToken(null);
            userDao.save(user);
        }
        return user;
    }

    /////Methode 2 ACTIVATE///////////
    /*public User activateUser(String token) {
        User user = userDao.findByVerificationToken(token);

        if (userDao.findByVerificationToken(token) == null || verificationTokenService.isExpired())
        { // Vérifier si le jeton est valide et non expiré
            throw new InvalidTokenException("Invalid or expired verification token");
        }
        else if (user != null) {
            user.setIsverified(1);
            user.setVerificationToken(null);
            userDao.save(user);
        }

        return user;
    }*/


    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public List<User> getAll(){
        return userDao.findAll();
    }

    public List<Role> getAllRole(){
        return roleDao.findAll();
    }
    public User findOne(String userName){
        return userDao.findById(userName).orElse(null);
    }
    public List<User> getUnverifiedUsers() {
        return userDao.findUnverifiedUsers();
    }
    public void delete(String userName){
        User u= userDao.findById(userName).orElse(null);
        u.getRole().clear();
        userDao.delete(u);
    }
    public void update(User user){
        userDao.save(user);
    }

    public long count(){
      long count=userDao.count();
      return count;
    }
    public long countadmin(){
        long countadmin=0;
        List<User> users=userDao.findAll();
        for(User user:users) {

            Set<Role> roles=user.getRole();
            Role role= roles.iterator().next();
            String rolename = role.getRoleName();
            if(rolename.equals("Admin")){
                countadmin+=1;
            }
        }
        return countadmin;
    }
    public long countoperateur(){
        long countoperateur=0;
        List<User> users=userDao.findAll();
        for(User user:users) {

        Set<Role> roles=user.getRole();
        Role role= roles.iterator().next();
        String rolename = role.getRoleName();
                if(rolename.equals("User")){
                    countoperateur+=1;
                    }
        }
            return countoperateur;
    }

    public User findByEmail(String Email) {
        return userDao.findByEmail(Email);
    }

   @Value("${spring.mail.username}")
    private String fromAddress;
    public void generatePasswordResetToken(String Email) {
        User user = userDao.findByEmail(Email);
        if (user == null) {
            throw new UserNotFoundException("No user found with Email: " + Email);
        }

        String token = generateToken();

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        passwordResetTokenRepository.save(passwordResetToken);

        String subject = "Password reset request";
        String text = "Please click the following link to reset your password: http://localhost:8081/forgot-password?token=" + token;
        emailService.sendEmail(user.getEmail(), subject, text);
    }

   private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void changePassword(String userName, String newPassword) {
        User user = userDao.findByUserName(userName);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setUserPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }
    public void ISVerified(String userName) {
      int  verified=1;
        User user = userDao.findByUserName(userName);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setIsverified(verified);
        userDao.save(user);
    }


// Reset Password:
   public boolean ifEmailExist(String UserEmail){
       return userDao.existsByEmail(UserEmail);
   }

    @Transactional
    public String getPasswordByUserEmail(String userEmail){
        return userDao.getPasswordByEmail(userEmail);
    }

    public User findByUserEmail(String UserEmail)
    {
        return this.userDao.findByEmail(UserEmail);
    }

    public void editUser(User user){
        this.userDao.save(user);
    }

    //reset password Using SMS:
    public User findByPhone(String phone)
    {
        return this.userDao.findByUserPhone(phone);
    }

    //Check Phone number in DB and send 6 digits code.
    public UserAccountResponse CheckSMS (UserResetPasswordSMS userResetPasswordSMS) {
        // Retrieve user using the entered phone number.
        User user = this.findByPhone(userResetPasswordSMS.getPhone());
        System.out.println("la variable User est " + user);
        System.out.println("la variable Phone est " + userResetPasswordSMS.getPhone());
        UserAccountResponse accountResponse = new UserAccountResponse();
        if (user != null) {
            String code = UserCode.getSmsCode();
            System.out.println("le code est" + code);
            this.smsService.SendSMS(userResetPasswordSMS.getPhone(),code);
            System.out.println("la variable User est" + user);
            user.setUserCode(code);
            userDao.save(user);
            accountResponse.setResult(1);
        } else {
            accountResponse.setResult(0);
        }
        return accountResponse;
    }



    //Compare given code with the one stored in DB and reset password.
    public UserAccountResponse resetPasswordSMS(UserNewPasswordSMS userNewPasswordSMS) {
        User user = this.findByPhone(userNewPasswordSMS.getPhone());
        UserAccountResponse accountResponse = new UserAccountResponse();
        if(user != null){
            if(user.getUserCode().equals(userNewPasswordSMS.getCode())){
                user.setUserPassword(passwordEncoder.encode(userNewPasswordSMS.getPassword()));
                user.setUserCode("expired");
                userDao.save(user);
                accountResponse.setResult(1);
            } else {
                accountResponse.setResult(0);
            }
        } else {
            accountResponse.setResult(0);
        }
        return accountResponse;
    }

}
