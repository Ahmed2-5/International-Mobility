package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Scheduel;
    private LocalDate EndDate;
    private int Status=0;
    @Enumerated(EnumType.STRING)
    private TypeScheduel typeScheduel;
    @JsonIgnore
    @OneToMany(mappedBy = "schedule")
    private List<Condidacy> candidates;
    @JsonIgnore
    @OneToMany(mappedBy = "scheduleSecondSelection")
    private List<QuizAttempt> quizAttempts;


}
