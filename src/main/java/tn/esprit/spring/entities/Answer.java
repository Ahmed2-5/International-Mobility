package tn.esprit.spring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Answer;

    private String content;
    private boolean correct;

    @ManyToOne
    private Question question;

    public Answer(String answerContent, boolean b, Question question) {
    }

    // getters and setters
}

