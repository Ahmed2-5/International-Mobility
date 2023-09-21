package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
