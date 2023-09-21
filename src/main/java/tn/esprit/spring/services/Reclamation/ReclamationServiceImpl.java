package tn.esprit.spring.services.Reclamation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.interfaces.IReclamationService;
import tn.esprit.spring.repositories.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ReclamationServiceImpl implements IReclamationService {

    @Autowired
    ApprenantRepository apprenantRepository;
    @Autowired
    ChambreRepository chambreRepository;
    @Autowired
    FoyerRepository foyerRepository;
    @Autowired
    QuestionRepRepository questionRepository;
    @Autowired
    ReponseRepository reponseRepository;
    @Autowired

    UserRepository userRepositoy;
    @Autowired
    RoleRepository repository;

    @Autowired
    RatingRepository ratingRepository;


    @Autowired
    ReclamationRepository reclamationRepository;







    @Override
    public void ajouterApprenant(Apprenant apprenant) {
        apprenantRepository.save(apprenant);
    }
    //-------------------------------------------QUESTION----------------------------------------------------//
    @Override
    public void AddQuestion(QuestRep questRep) {
        questionRepository.save(questRep);
    }

    @Override
    public List < QuestRep > findAllQuestions() {
        return questionRepository.findAll();
    }

    @Transactional
    @Override
    public QuestRep UpdateQuestion(QuestRep questRep) {

        return questionRepository.save(questRep);
    }

    @Override
    public Optional < QuestRep > findQuestionById(Integer id) {
        return questionRepository.findById(id);
    }

    @Override
    public void deleteQuestionById(Integer id) {
        questionRepository.deleteById(id);
    }
    //-------------------------------------------REPONSE----------------------------------------------------//
    @Override
    public void AddReponse(Reponse reponse) {
        reponseRepository.save(reponse);

    }

    @Override
    public void AddReponseAndAssignTOQuestion(Reponse rep, Integer IdQuestion) {

        QuestRep questRep = questionRepository.findById(IdQuestion).orElse(null);

        rep.setQuestRep(questRep);
        reponseRepository.save(rep);

    }

    @Override
    public List < Reponse > findAllReponse() {
        return reponseRepository.findAll();
    }

    @Override
    public Optional < Reponse > findReponseById(Integer id) {
        return reponseRepository.findById(id);
    }

    @Override
    public Reponse UpdateReponse(Reponse reponse) {
        return reponseRepository.save(reponse);
    }

    @Override
    public void deleteReponseById(Integer id) {
        reponseRepository.deleteById(id);
    }

    @Override

    public List < Reponse > getReponsesByHighestAverageRAting() {
        List < Reponse > reponses = reponseRepository.findAll();
        Map < Reponse, Double > averageRatingsMap = new HashMap < > ();
        for (Reponse reponse: reponses) {
            OptionalDouble averageRating = reponse.getRatings().stream().mapToInt(Rating::getValue).average();
            double averageRatingValue = averageRating.isPresent() ? averageRating.getAsDouble() : 0.0;
            averageRatingsMap.put(reponse, averageRatingValue);
        }
        return averageRatingsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    //-------------------------------------------RATING----------------------------------------------------//
    @Override
    public Rating CreateRating(Rating rating, Integer idReponse) {
        Reponse reponse = reponseRepository.findById(idReponse).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating Not found"));
        rating.setReponse(reponse);
        return ratingRepository.save(rating);

    }

    @Override
    public Optional < Rating > findRatingById(Long idRating) {
        return ratingRepository.findById(idRating);
    }

    @Override
    public Rating UpdateRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public List < Rating > GetAllRAtingsByValue(int value) {
        return ratingRepository.findRatingByValue(value);
    }
    //-------------------------------------------RECLAMATION----------------------------------------------------//
    @Override
    @Transactional
    public Reclamation AddReclamation(Reclamation reclamation, String userName) {
        User user = userRepositoy.findByUserName(userName);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        reclamation.setUser(user);
        return reclamationRepository.save(reclamation);
    }
    @Override
    public Reclamation changerStatuReclamationTraitement(String admin, Long id, String statu) {
        if (admin == null && id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Null pointer exception");
        } else {
            Reclamation reclamation1 = reclamationRepository.findById(id).orElse(null);
            reclamation1.setUserAdmin(userRepositoy.findByUserName(admin));
            User admin1 = userRepositoy.findByUserName(admin);
            List < Reclamation > l = admin1.getReclamationsAtraiter();
            l.add(reclamation1);
            admin1.setReclamationsAtraiter(l);

            if (statu.equals(Statu.EN_COURS.toString())) {
                reclamation1.setStatu(Statu.EN_COURS.toString());

            } else if (statu.equals(Statu.REJETEE.toString())) {
                reclamation1.setStatu(Statu.REJETEE.toString());

            } else if (statu.equals(Statu.VERIFIE.toString())) {
                reclamation1.setStatu(Statu.VERIFIE.toString());

            }
            userRepositoy.save(admin1);
            reclamationRepository.save(reclamation1);
            return reclamation1;
        }
    }

    @Override
    public List < Reclamation > getReclamationByUserName(String userName) {
        User user = userRepositoy.findByUserName(userName);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usernot found");
        }
        return user.getReclamations();
    }

    @Override
    public List < Reclamation > getReclamatinByGender(String gender) {
        List < Reclamation > reclamations = reclamationRepository.findAll();
        return reclamations.stream().filter(reclamation -> reclamation.getUser().getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }

    @Override
    public List < Reclamation > findAllRec(Reclamation reclamation) {
        return reclamationRepository.findAll();
    }

    @Override
    public Optional < Reclamation > getReclamationById(Long idRec) {
        return reclamationRepository.findById(idRec);
    }

    @Transactional
    @Override
    public List < Reclamation > getReclamationByStatus(String status) {
        return reclamationRepository.findByStatus(status);
    }

    public List < Object[] > getReclamationCountByGender() {
        return reclamationRepository.countByUserGender();
    }

    public List < Object[] > getReclamationCountByObjet() {
        return reclamationRepository.countByObjet();
    }
    //-------------------------------------------USER----------------------------------------------------//
    @Override
    public void AddUser(User user) {
        userRepositoy.save(user);
    }
    //-------------------------------------------FOYER----------------------------------------------------//
    @Override
    public void foyerAdd (Foyer foyer){
         foyerRepository.save(foyer);
    }

    @Override
    public Chambre chambreAdd(Chambre chambre,Long id){
        Foyer foyer=foyerRepository.findById(id).orElse(null);
        chambre.setFoyer(foyer);
        List<Chambre> chambreList=foyer.getChambreList();
        chambreList.add(chambre);
        foyer.setChambreList(chambreList);
        foyer.setPlaceLibre(foyer.getPlaceLibre()-1);
        chambreRepository.save(chambre);
        foyerRepository.save(foyer);

        return chambre;
    }

    @Override
    public Optional<Foyer> getFoyer(Long id){
        return foyerRepository.findById(id);
    }



    @Override
    public Optional<Chambre> getchambre(Long id){
        return  chambreRepository.findById(id);
    }
    public List<Foyer> getAllFoyer()
    {
        return foyerRepository.findAll();
    }
   @Override
    public Chambre affectationEtudiantFoyerChambreSurDemande(String username)
    {
        User etudiant = userRepositoy.findByUserName(username);
        if (etudiant==null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        }
        else
        {
            String pays = etudiant.getPaysMobility();
            for (Foyer f:getAllFoyer())
            {
                if (pays.equals(f.getPays()) && f.getPays()!=null)
                {
                    if (f.getPlaceLibre()!=0)
                    {

                        for (Chambre i:f.getChambreList())
                        {

                            if (i.getPlaceLibre()!=0 && i.getGender().equals(etudiant.getGender()))
                            {

                                Optional<Chambre> chambre = chambreRepository.findById(i.getId());

                                chambre.get().setPlaceLibre(chambre.get().getPlaceLibre()-1);

                                List<User> users = chambre.get().getEtudiantList();

                                users.add(etudiant);
                                chambre.get().setEtudiantList(users);
                                etudiant.setChambre(chambre.get());
                                chambreRepository.save(chambre.get());
                                System.out.println("5");
                                userRepositoy.save(etudiant);
                                System.out.println("6");

                                return chambre.get();
                            }
                        }
                    }
                }
            }

        }
        return null;
    }
    @Override
    public void deleteFoyerById(Long idfoyer){
        foyerRepository.deleteById(idfoyer);
    }
    public Chambre getChambreByUser(String userName){
        User user=userRepositoy.findByUserName(userName);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found");
        }
        return user.getChambre();
    }

    @Override
    public Chambre removeEtudiantFromChambre(Long chambreId,String username){
        Optional<Chambre> optionalChambre=chambreRepository.findById(chambreId);
        if(optionalChambre.isPresent()){
            Chambre chambre =optionalChambre.get();
            List<User> etudiantList = chambre.getEtudiantList();
            User etudiant = userRepositoy.findByUserName(username);
            if (etudiant !=null){
                etudiantList.remove(etudiant);
                chambre.setEtudiantList(etudiantList);
                chambre.setPlaceLibre(chambre.getPlaceLibre()+1);
                chambreRepository.save(chambre);
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found ");
            }
            return chambre;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Room Not found");
        }
    }

    @Override
    public void removeChambreFromFoyer(Long foyerId, Long chambreId){
        Foyer foyer =foyerRepository.findById(foyerId).orElse(null);
        if (foyer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Foyer not found");
        }
        Chambre chambre = chambreRepository.findById(chambreId).orElse(null);
        if (chambre == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"chambre not found");
        }
        foyer.getChambreList().remove(chambre);
        chambre.setFoyer(null);
        chambreRepository.save(chambre);
        foyer.setPlaceLibre(foyer.getPlaceLibre()+1);
        foyerRepository.save(foyer);
    }


    //-------------------------------------------Bourse----------------------------------------------------//





}
