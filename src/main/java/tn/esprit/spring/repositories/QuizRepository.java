package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Quiz;
@Repository

public interface QuizRepository extends JpaRepository<Quiz,Integer> {
}
