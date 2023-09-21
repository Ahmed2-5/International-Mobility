package tn.esprit.spring.interfaces;

import tn.esprit.spring.entities.*;

import java.util.List;
import java.util.Optional;

public interface IReclamationService {
    void ajouterApprenant(Apprenant apprenant);

    //tebaa les question el koll.
    void AddQuestion(QuestRep questRep);
    public List < QuestRep > findAllQuestions();

    public QuestRep UpdateQuestion(QuestRep questRep);
    public Optional < QuestRep > findQuestionById(Integer id);

    public void deleteQuestionById(Integer id);
    //   public List<QuestRep> getQuestionByType(Type type);

    //tebaa les reponse el koul
    void AddReponse(Reponse reponse);

    public void AddReponseAndAssignTOQuestion(Reponse rep, Integer IdQuestion);

    public List < Reponse > findAllReponse();

    public Reponse UpdateReponse(Reponse reponse);

    public Optional < Reponse > findReponseById(Integer id);

    public void deleteReponseById(Integer id);

    List < Reponse > getReponsesByHighestAverageRAting();

    //////////tebaa les Rating

    public Rating CreateRating(Rating rating, Integer idReponse);
    public Optional < Rating > findRatingById(Long idRating);

    public Rating UpdateRating(Rating rating);

    public List < Rating > GetAllRAtingsByValue(int value);

    public Reclamation AddReclamation(Reclamation reclamation, String userName);

    public List < Reclamation > getReclamationByUserName(String userName);

    public List < Reclamation > getReclamatinByGender(String gender);

    public List < Reclamation > findAllRec(Reclamation reclamation);

    public Optional < Reclamation > getReclamationById(Long idRec);
    public Reclamation changerStatuReclamationTraitement(String admin, Long id, String statu);

    public List < Reclamation > getReclamationByStatus(String status);

    public List < Object[] > getReclamationCountByGender();

    public List < Object[] > getReclamationCountByObjet();
    void AddUser(User user);
    public Chambre chambreAdd(Chambre chambre,Long id);
    void  foyerAdd(Foyer foyer);

    public Optional<Foyer> getFoyer(Long id);
    public List<Foyer> getAllFoyer( );
    public Optional<Chambre> getchambre(Long id);
    public Chambre affectationEtudiantFoyerChambreSurDemande(String id);
    public void deleteFoyerById(Long idfoyer);
    public Chambre getChambreByUser(String userName);

    public Chambre removeEtudiantFromChambre(Long chambreId,String username);

    public void removeChambreFromFoyer(Long foyerId, Long chambreId);

}