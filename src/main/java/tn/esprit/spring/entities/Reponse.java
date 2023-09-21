package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reponse implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer IdReponse ;
    private String Reponse ;

    private Integer Avis ;


    @OneToOne
    private QuestRep questRep;




    @JsonIgnore
    @OneToMany(mappedBy = "reponse", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();








}
