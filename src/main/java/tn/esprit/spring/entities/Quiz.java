package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Quiz;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Description is mandatory")

    private String description;
    @NotNull(message = "Duration is mandatory")

    private int Duration;

    @JsonIgnore
    @OneToOne
    private Opportunity opportunity;
    @Size(min = 1, message = "Quiz must have at least one question")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "quiz_question",
            joinColumns = {@JoinColumn(name = "quiz_id")},
            inverseJoinColumns = {@JoinColumn(name = "question_id")}
    )
    private Set<Question> questions = new HashSet<>();


    private LocalDate startDate;
    @Min(value = 1, message = "Quiz must have at least 1 question")
    @Max(value = 100, message = "Quiz can have up to 100 questions")
    private int nbQuestion;
    @NotNull(message = "End date is mandatory")


    private LocalDate endDate;

    public Quiz(String title, String description, Opportunity opportunity, List<Question> questions, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.opportunity = opportunity;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
