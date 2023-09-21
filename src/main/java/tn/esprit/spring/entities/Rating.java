package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idRate;

    @Min(value = 0, message = "Rating value should not be less than 0")
    @Max(value = 5, message = "Rating value should not be greater than 5")
    private int value;

    private String Comment;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Reponse reponse;

}
