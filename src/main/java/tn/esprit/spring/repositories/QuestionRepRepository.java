package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.entities.QuestRep;

public interface QuestionRepRepository extends JpaRepository<QuestRep,Integer> {


 //   List<QuestRep> findQuestRepByType(Type type);


}
