package tn.esprit.spring.interfaces;

public interface EmailService {
    public void sendEmail(String to, String subject, String body);
}
