package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Condidacy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id_Condidacy;

    @NotNull(message = "La moyenne de la 1ère année est obligatoire")
    @DecimalMin(value = "0.0", message = "La moyenne de la 1ère année doit être supérieure ou égale à 0.0")
    @DecimalMax(value = "20.0", message = "La moyenne de la 1ère année doit être inférieure ou égale à 20.0")
    private float Moyenne_1year;

    @NotNull(message = "La moyenne de la 1ère année est obligatoire")
    @DecimalMin(value = "0.0", message = "La moyenne de la 1ère année doit être supérieure ou égale à 0.0")
    @DecimalMax(value = "20.0", message = "La moyenne de la 1ère année doit être inférieure ou égale à 20.0")
    private float Moyenne_2year;

    @NotNull(message = "La moyenne de la 1ère année est obligatoire")
    @DecimalMin(value = "0.0", message = "La moyenne de la 1ère année doit être supérieure ou égale à 0.0")
    @DecimalMax(value = "20.0", message = "La moyenne de la 1ère année doit être inférieure ou égale à 20.0")
    private float Moyenne_3year;
    @JsonIgnore
    private float score;

    private boolean attempted = false;
    @NotBlank(message = "La description de la motivation est obligatoire")
    @Size(max = 500, message = "La description de la motivation doit contenir au maximum 500 caractères")
    private String MotivationDescription;
    @Enumerated(EnumType.STRING)

    private status Status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")

    private User user;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;
    @JsonIgnore

    @OneToOne(mappedBy = "condidacy", cascade = CascadeType.ALL)
    private QuizAttempt quizAttempt;
    @JsonIgnore

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;


}
