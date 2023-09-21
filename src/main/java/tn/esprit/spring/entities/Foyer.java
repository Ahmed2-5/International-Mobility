package tn.esprit.spring.entities;

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
public class Foyer implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String pays;
    private Integer capacite;
    private int placeLibre;




    @OneToMany(mappedBy = "foyer",cascade = CascadeType.ALL)
    private List<Chambre> chambreList=new ArrayList<>();
}