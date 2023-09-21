package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reclamation implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long IdReclam;
    @NonNull
    private String Comment;
    private String Objet;

    private String statu=Statu.NON_TRAITE.toString();
    private LocalDateTime dateReclamation=LocalDateTime.now() ;
    private String response=null;

    @JsonIgnore
    @ManyToOne()
    private User user;

    @JsonIgnore
    @ManyToOne()
    private User userAdmin=null;
}
