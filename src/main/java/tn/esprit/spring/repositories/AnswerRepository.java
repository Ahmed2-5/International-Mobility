package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Answer;
@Repository
public interface AnswerRepository extends JpaRepository<Answer ,Integer> {
}
