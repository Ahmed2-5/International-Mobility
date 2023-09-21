package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "appointement")
//@EntityListeners(AppointementListener.class)
public class Appointement  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NonNull
    private Integer idAppointement;
    private String firstname;
    private String lastname;
    private String email;
    private Integer PhoneNumber;
    // @Temporal(TemporalType.DATE)
    private LocalDate dateDemande;
   //@Temporal(TemporalType.TIMESTAMP)
    private LocalDate dateRdv;
    private Boolean status ;

    @ToString.Exclude
    @ManyToOne
    private Appointement rdv;
    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Appointement app;

    public void setRole(Role role) {
    }



  /*  public void sendReminderEmail(EmailService emailService) throws MessagingException {
        String to = this.email;
        String subject = "Appointment Reminder";
        String body = "Hello " + this.firstname + ",\n\nThis is a reminder that you have an appointment scheduled for " + this.dateRdv.toString() + ".\n\nThank you.";
        emailService.sendEmail(to, subject, body);
    }*/
}
