package tn.esprit.spring.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserADS {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long adsID;
    private String addstitle;
    private LocalDate start_Date;
    private LocalDate end_Date;
    private String unyName;
    private int status;
    private int numberOfRatings;
    private double averageRating;
    /* @ManyToOne
     private  User user;*/
  @OneToOne
   private Opportunity opportunity;





}
