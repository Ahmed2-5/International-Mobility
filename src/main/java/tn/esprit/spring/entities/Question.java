package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdQuestion;
    private String content;
    @Enumerated(EnumType.STRING)
    private Speciality specialty;
    @JsonIgnore

    @ManyToMany(mappedBy = "questions")
    private Set<Quiz> quizzes = new HashSet<>();
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;
    private int Point;

    private String image;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userQ")
    private User userQ;


    public Question(String content) {
        this.content = content;
    }


}
