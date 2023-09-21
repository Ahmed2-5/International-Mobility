package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Comment;
import tn.esprit.spring.entities.RatingForum;
import tn.esprit.spring.entities.User;

@Repository
public interface RatingForumRepository extends JpaRepository<RatingForum,Integer> {

    RatingForum findByUserAndComment(User u, Comment c);
}
