package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int idPost;
    private String content;
    private Date publishDate;
    private String title;
    @JsonIgnore
    @Lob
    private byte[] imageUrl;

    @JsonIgnore
    @ManyToOne
    private User user;



    private int nbrRate ;
    private float rate ;



}
