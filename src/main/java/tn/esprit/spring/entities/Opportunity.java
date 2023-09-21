package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Opportunity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id_Opportunity;
    @NotNull
    @Positive
    private int Capacity;
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate StarDate;
    @NotNull
    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate EndDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeOpp Type;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Grade grade;
    @Enumerated(EnumType.STRING)
    private Speciality specialite;
    @Positive
    private float needs;
    @NotBlank
    @Size(max = 500)
    private String Description;
    @NotBlank
    @Size(max = 100)
    private String Title;
    @NotNull
    @Positive
    private float Coef1stYear;
    @NotNull
    @Positive
    private float Coef2stYear;
    @NotNull
    @Positive
    private float Coef3stYear;
   /* @Column(length = 1000)
    @JsonIgnore
    private String qrContent;
    @JsonIgnore
    @Column(length = 1000)
    private byte[] qrCodeImage;*/
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    @JsonIgnore

    @OneToOne(mappedBy = "opportunity", cascade = CascadeType.ALL)
    private Quiz quizzesQuiz;
    @JsonIgnore
    private double averageRating;
    @JsonIgnore
    private int numberOfRatings;

    @OneToOne(mappedBy = "opportunity")
    private UserADS userADS;
  /*  public void setQrCodeImage(byte[] qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }*/


}
