package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Question;
@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer> {
}
