package tn.esprit.spring.entities;

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
public class WaitingList implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NonNull
    private Integer idWaiting;
    private String email;
    private Integer PhoneNumber;
    private LocalDate dateDemande;
    private Float Paiment;


    public Integer getIdWaiting() {
        return idWaiting;
    }

    public void setIdWaiting(Integer idWaiting) {
        this.idWaiting = idWaiting;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Float getPaiment() {
        return Paiment;
    }

    public void setPaiment(Float paiment) {
        Paiment = paiment;
    }

    public Appointement getAppointement() {
        return appointement;
    }

    public void setAppointement(Appointement appointement) {
        this.appointement = appointement;
    }

    @OneToOne
    private Appointement appointement ;


}
