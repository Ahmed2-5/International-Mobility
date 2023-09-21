package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttempt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idQuizAttempt;
    @JsonIgnore
    @OneToOne
    private Condidacy condidacy;
    @JsonIgnore
    @OneToOne
    private Quiz quiz;
    @JsonIgnore
    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<AnswerAttempt> answerAttempts;

    private float score;
    private LocalDate StartTime;
    private LocalDateTime EndTime;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule scheduleSecondSelection;

    private String captcha;

    // getters and setters


}
