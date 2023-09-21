package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Chambre implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private int numeroR;
    private int capacity;
    private Integer placeLibre;
    private String gender;

    @OneToMany(mappedBy = "chambre")
    private List<User> etudiantList = new ArrayList<User>(capacity);

    @JsonIgnore
    @ManyToOne()
    private Foyer foyer;
}
