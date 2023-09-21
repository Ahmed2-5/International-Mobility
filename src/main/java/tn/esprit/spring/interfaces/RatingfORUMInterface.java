package tn.esprit.spring.interfaces;

import tn.esprit.spring.entities.Comment;

public interface RatingfORUMInterface {
          Comment addLike(String iduser, int idComment);

          Comment addDesLike(String  r, int idComment);


}

