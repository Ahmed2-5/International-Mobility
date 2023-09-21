package tn.esprit.spring.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.UserRepository;
import tn.esprit.spring.services.User.EmailService;
import tn.esprit.spring.services.User.UserService;
import tn.esprit.spring.services.User.VerificationTokenService;
import tn.esprit.spring.util.UserCode;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenService verificationTokenService;

    @PostMapping("/registerNewUser")
    public User createUser(@RequestBody User user) {
        User savedUser = userService.registerNewUser(user);

        UserVerificationToken verificationToken = verificationTokenService.createVerificationToken(user); // création du jeton de vérification
        verificationTokenService.saveVerificationToken(verificationToken);
        return savedUser;
    }
   /* @PostMapping("/registerNewUser")
    public User createUser(@RequestParam("userName") String userName,@RequestParam("userFirstName") String userFirstName,
                           @RequestParam("userLastName") String userLastName,@RequestParam("userPassword") String userPassword
                            ,@RequestParam("email") String email  ,@RequestParam("userPhone") String userPhone
                            , @RequestParam("CV") MultipartFile CV ,@RequestParam("Role") String role) {

      User user= new User();
      user.setUserName(userName);
      userService.AffectUserToRole(role,user);
      for (Role role2:user.getRole())
      {
          log.info("role1hhhhhhhhhhhhhhhhhhhhhhh"+role2.getRoleName());
      }

      user.setCV(CV.getOriginalFilename());
      user.setUserFirstName(userFirstName);
      user.setUserLastName(userLastName);
      user.setUserPassword(userPassword);
      user.setEmail(email);
      user.setUserPhone(userPhone);

      User savedUser = userService.registerNewUser(user);

        VerificationToken verificationToken = verificationTokenService.createVerificationToken(user); // création du jeton de vérification
        verificationTokenService.saveVerificationToken(verificationToken);
        return savedUser;
    }*/
    @PutMapping("/activate/{verificationToken}")
    public ResponseEntity activateAccount(@PathVariable String verificationToken) {
        User user = userService.activateUser(verificationToken);
        if (user != null) {
             String to = user.getEmail();
             String subject = "Account Created";
             String text = "Your account has been created successfully.";
            emailService.sendEmail(to, subject, text);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }

  /*  @PostMapping({"/registerNewUser"})
    public User registerNewUser(@RequestBody User user) {
        return userService.registerNewUser(user);
    }*/

    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin(){
        return "This URL is only accessible to the admin";
    }

    @GetMapping({"/users"})
    @PreAuthorize("hasRole('Admin')")
    public List<User> getAll(){
        return userService.getAll();
    }
    @GetMapping({"/roles"})
    @PreAuthorize("hasRole('Admin')")
    public List<Role> getAllRole(){
        return userService.getAllRole();
    }
    @GetMapping({"/user/{userName}"})
    public User findOne(@PathVariable String userName){
        return userService.findOne(userName);
    }

    @GetMapping("/unverified-users")
    @PreAuthorize("hasRole('Admin')")
    public List<User> getUnverifiedUsers() {
      return   userService.getUnverifiedUsers();
    }

    @GetMapping({"/forUser"})
    @PreAuthorize("hasRole('User')")
    public String forUser(){
        return "This URL is only accessible to the user";
    }

    @DeleteMapping ({"/delete/{userName}"})
    @PreAuthorize("hasRole('Admin')")
    public void delete(@PathVariable String userName){
        userService.delete(userName);
    }

    @PutMapping  ({"/update"})
    @PreAuthorize("hasRole('User')")
    public void update(@RequestBody User user){
        userService.update(user);
    }
    @GetMapping("/count")
    @PreAuthorize("hasRole('Admin')")
    public long count(){return userService.count();}
    @GetMapping("/countoperateur")
    @PreAuthorize("hasRole('Admin')")
    public long countoperateur(){return userService.countoperateur();}

    @PutMapping("/change-password/{userName}/{newPassword}")
    public ResponseEntity<Void> changePassword(@PathVariable String userName, @PathVariable String newPassword) {
        userService.changePassword(userName, newPassword);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasRole('Admin')")
    public long countadmin(){return userService.countadmin();}

  @PutMapping("/VerifUser/{userName}")
  @PreAuthorize("hasRole('Admin')")
  public void VerifUser(@PathVariable String userName) {

      userService.ISVerified(userName);
  }

    // http://localhost:8080/checkEmail
    @PostMapping("/checkEmail")
    public UserAccountResponse resetPasswordEmail(@RequestBody UserResetPassword resetPassword){
        User user = this.userService.findByUserEmail(resetPassword.getEmail());
        UserAccountResponse accountResponse = new UserAccountResponse();
        if(user != null){
            String code = UserCode.getCode();
            System.out.println("le code est" + code);
            UserMail mail = new UserMail(resetPassword.getEmail(),code);
            System.out.println("le mail est" + resetPassword.getEmail());
            System.out.println("la variable mail est" + mail);
            emailService.sendCodeByMail(mail);
            System.out.println("la variable User est" + user);
            user.setUserCode(code);
            userDao.save(user);
            accountResponse.setResult(1);
        } else {
            accountResponse.setResult(0);
        }
        return accountResponse;
    }

    // http://localhost:8080/resetPassword
    @PostMapping("/resetPassword")
    public UserAccountResponse resetPassword(@RequestBody UserNewPassword newPassword){
        User user = this.userService.findByUserEmail(newPassword.getEmail());
        UserAccountResponse accountResponse = new UserAccountResponse();
        if(user != null){
            if(user.getUserCode().equals(newPassword.getCode())){
                user.setUserPassword(passwordEncoder.encode(newPassword.getPassword()));
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


    @PostMapping("/checkSMS")
    public UserAccountResponse CheckSMS (@RequestBody UserResetPasswordSMS userResetPasswordSMS) {
        return userService.CheckSMS(userResetPasswordSMS);
    }
    @PostMapping("/resetPasswordSMS")
    public UserAccountResponse resetPasswordSMS (@RequestBody UserNewPasswordSMS newPassword) {
        return userService.resetPasswordSMS(newPassword);
    }


}