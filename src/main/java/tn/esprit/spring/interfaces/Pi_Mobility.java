package tn.esprit.spring.interfaces;

import com.google.zxing.WriterException;
import tn.esprit.spring.entities.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Pi_Mobility {
    public List<Opportunity> findAllOpportunities();

    public Optional<Opportunity> findOpportunityById(Integer id);

    public Opportunity createOpportunity(Opportunity opportunity, String id_Partner) throws IOException, WriterException;

    public Opportunity updateOpportunity(Opportunity opportunity);

    public void deleteOpportunityById(Integer id);

    public List<Condidacy> findAllCandidates();

    public Optional<Condidacy> findCandidateById(Integer id);

    public Condidacy createCandidateAndAssignEtudiant(Condidacy condidacy, String Id_Student, int Id_Opportunity) throws MessagingException, IOException;

    public Condidacy updateCandidate(Condidacy condidacy);

    public void deleteCandidateById(Integer id);

    public List<Opportunity> RetreiveOpportunitiesForStudentbySpecialite(String id_Student);

    public List<Condidacy> RtreiveStudentCondidacies(String id_Student); //Reste

    Condidacy UpdateCondidacy(Integer id_Condidacy);

    List<Condidacy> trierEtudiantsParScore(Integer Id_Opportunity);

    List<Condidacy> CalculScore(Integer Id_Opportunity);

    void sendEmailToTopNCandidates(int n, int opportunityId) throws MessagingException;

    public List<Condidacy> getTopNCandidatures(Integer opportunityId);

    public void sendSelectedCandidatesEmails(Integer opportunityId);

    List<Condidacy> sendSelectedCandidatesEmailsTest(Integer opportunityId) throws MessagingException, IOException;

   void  ajouterQuizAvecQuestionsEtReponses(Quiz quiz);
    public void ajouterQuestion(Question question, Integer quizId) ;
    public void ajouterReponse(Integer questionId,Answer reponse) ;
    public void ajouterQuiz(Quiz quiz,Integer OpportunityId) ;
    public List<Condidacy> sendSelectedCandidatesEmailsQuiz(Integer opportunityId) throws MessagingException, IOException ;
List<Question> RtreiveQuestionOfQuizBySpeciality(String UserName,Integer id_Quiz);
    public Map<String, Double> getAvgQuizScoreByOpportunityType() ;



    }
