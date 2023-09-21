package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
//@Table(name = "historique")
@NoArgsConstructor
@AllArgsConstructor
public class Historique implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NonNull
    private Integer idAppointement;
    private String email;
    private Integer PhoneNumber;
    // @Temporal(TemporalType.DATE)
    private LocalDate dateRdv;
    private Integer DurationAppointment;
    private  String NamePartner;
    private  String Report;
    @OneToMany(mappedBy="app")
    @JsonIgnore
    @ToString.Exclude
    private Set<Appointement> Appointements;







}
