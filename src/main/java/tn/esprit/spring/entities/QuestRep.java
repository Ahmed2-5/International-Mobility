package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestRep implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer IdQuestion ;
    private String Question ;
    @Enumerated(EnumType.STRING)
    private Type Type ;




    @ManyToOne

    @JoinColumn(name = "userQ")

    private User userQ;



    @OneToOne(mappedBy = "questRep",cascade = CascadeType.REMOVE)
    private Reponse reponse;









}
