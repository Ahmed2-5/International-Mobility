package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Opportunity;
import tn.esprit.spring.entities.QuizAttempt;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt,Integer> {
    @Query("SELECT oa.condidacy.opportunity.specialite, AVG(oa.score) " +
            "FROM QuizAttempt oa " +
            "GROUP BY oa.condidacy.opportunity.specialite")
    List<Object[]> findAvgQuizScoreByOpportunityType();
    List<QuizAttempt> findByCondidacy_Opportunity(Opportunity opportunity);
    @Query("SELECT AVG(TIME_TO_SEC(TIMEDIFF(qa.EndTime, qa.StartTime))) FROM QuizAttempt qa WHERE qa.quiz.id_Quiz = :quizId")
    Double getAverageCompletionTimeByQuiz(@Param("quizId") Integer quizId);
}

