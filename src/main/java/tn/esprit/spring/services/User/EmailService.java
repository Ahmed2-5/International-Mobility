package tn.esprit.spring.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.UserMail;
import tn.esprit.spring.repositories.IUserEmailRepository;

import java.util.Random;
@Service
public class EmailService implements IUserEmailRepository {


    @Autowired UserService userservice;
    @Autowired
    private JavaMailSender userMailSender;
    @Autowired
    private VerificationTokenService verificationTokenService;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        userMailSender.send(message);
    }
    public void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        user.setVerificationToken(verificationTokenService.generateVerificationToken());
        message.setSubject("Vérification du compte");
        message.setText("Bonjour " + user.getUserFirstName() + ",\n\n" +
                "Veuillez cliquer sur le lien ci-dessous pour activer votre compte :\n\n" +
                "http://localhost:8081/activate?token=" + user.getVerificationToken());
        userMailSender.send(message);
    }
@Override
    public void sendCodeByMail(UserMail mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("departement.mobility@gmail.com");
        simpleMailMessage.setTo(mail.getTo());
        simpleMailMessage.setSubject("Code Active");
        simpleMailMessage.setText(mail.getCode());
        userMailSender.send(simpleMailMessage);
    }








}
