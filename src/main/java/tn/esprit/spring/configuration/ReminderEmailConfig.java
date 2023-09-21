package tn.esprit.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.spring.entities.Appointement;
import tn.esprit.spring.repositories.AppointementRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Configuration
public class ReminderEmailConfig {
    @Autowired
    private AppointementRepository appointementRepository;

    @Scheduled(cron = "0 * * * * ?") // Run every day at 12 PM
    public void sendReminderEmails() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Appointement> appointments = appointementRepository.findByDateRdv(tomorrow);

        for (Appointement appointment : appointments) {
            sendReminderEmail(appointment);
        }
    }

    public void sendReminderEmail(Appointement appointment) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        if (appointment.getDateRdv().equals(tomorrow) && appointment.getStatus()) {
            // configure the email properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // create a session with the email properties and authenticate the sender
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("departement.mobility@gmail.com", "icjqzfwnuqscwffs");
                }
            });

            try {
                // create a message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("departement.mobility@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(appointment.getEmail()));
                message.setSubject("Appointment Reminder");
                message.setText("Dear " + appointment.getFirstname() + " " + appointment.getLastname() + ",\n\n"
                        + "This is a reminder that your appointment is scheduled for tomorrow, "
                        + appointment.getDateRdv().toString() + ".\n\n"
                        + "Please let us know if you need to reschedule or cancel the appointment.\n\n"
                        + "Thank you,\n"
                        + "Your Appointment Team");

                // send the message
                Transport.send(message);

                System.out.println("Reminder email sent to " + appointment.getEmail());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }}

