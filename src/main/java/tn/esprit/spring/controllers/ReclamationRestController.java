package tn.esprit.spring.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.interfaces.IReclamationService;
import tn.esprit.spring.repositories.ReponseRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Controller
@ComponentScan(basePackages = "tn.esprit.spring.controllers")
@RestController
@RequestMapping("/examen")
public class ReclamationRestController {
	@Autowired
	IReclamationService IReclamationService;
	@Autowired
	private ReponseRepository reponseRepository;




	@PostMapping("/add-apprenant")
	public void addApprenant(@RequestBody Apprenant apprenant) {
		IReclamationService.ajouterApprenant(apprenant);
	}

	//tebaa les question el koll

	@PostMapping("/Question/AddQuestion")
	public void AddQuestion(@RequestBody QuestRep questRep) {
		System.out.println(questRep);
		IReclamationService.AddQuestion(questRep);
	}

	@GetMapping("/Question/getQuestionById/{idQuestion}")
	public Optional < QuestRep > findQuestionById(@PathVariable Integer idQuestion){
		return IReclamationService.findQuestionById(idQuestion);

	}

	@GetMapping("/Question/GetAll")
	public List < QuestRep > findAllQuestions() {

		return IReclamationService.findAllQuestions();
	}

	@Transactional
	@PutMapping("/Question/UpdateQuestion/{id}")
	public ResponseEntity < QuestRep > UpdateQuestion(@PathVariable Integer id, @RequestBody QuestRep questRep) {

		Optional < QuestRep > questionOptional = IReclamationService.findQuestionById(id);
		if (questionOptional.isPresent()) {
			questRep.setIdQuestion(id);
			return ResponseEntity.ok(IReclamationService.UpdateQuestion(questRep));

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/Question/DeleteQuestion/{id}")
	public ResponseEntity < Void > deleteQuestion(@PathVariable Integer id) {
		Optional < QuestRep > questionOptional = IReclamationService.findQuestionById(id);
		if (questionOptional.isPresent()) {
			IReclamationService.deleteQuestionById(id);
			return ResponseEntity.noContent().build();

		} else {
			return ResponseEntity.notFound().build();
		}
	}

  /*	@GetMapping("/Question/getQuestionByType")
  	public List<QuestRep> getQuestionByType(@RequestParam Type type){
  		return Pi_Mobility.getQuestionByType(type);
  	}
  */

	//tebaa les reponse el koul

	@PostMapping("/Reponse/AddAndAssignReponseToQuestion/{IdQuestion}")
	void AddReponseAndAssignTOQuestion(@RequestBody Reponse rep, @PathVariable Integer IdQuestion) {
		IReclamationService.AddReponseAndAssignTOQuestion(rep, IdQuestion);

	}

	@GetMapping("/Reponse/FindAll")
	public List < Reponse > findAllReponse() {
		return IReclamationService.findAllReponse();
	}

	@PutMapping("/Reponse/UpdateReponse/{id}")
	public ResponseEntity < Reponse > UpdadeReponse(@PathVariable Integer id, @RequestBody Reponse reponse) {
		Optional < Reponse > reponseOptional = IReclamationService.findReponseById(id);

		if (reponseOptional.isPresent()) {
			reponse.setIdReponse(id);
			return ResponseEntity.ok(IReclamationService.UpdateReponse(reponse));
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@DeleteMapping("/Reponse/deleteReponse/{id}")
	public ResponseEntity < Void > deleteReponse(@PathVariable Integer id) {
		Optional < Reponse > reponseOptional = IReclamationService.findReponseById(id);
		if (reponseOptional.isPresent()) {
			IReclamationService.deleteReponseById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/Reponse/getReponse_highest-rating")
	public ResponseEntity < Reponse > getReponseWithHighestRating() {
		List < Reponse > reponses = reponseRepository.findReponseWithHighestRating();
		if (reponses.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(reponses.get(0));
	}

	@GetMapping("/Reponse/GetRponse_highestAverage")
	public ResponseEntity < List < Reponse >> getReponsesByHighestAverageRAting() {
		List < Reponse > reponses = IReclamationService.getReponsesByHighestAverageRAting();
		if (reponses.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(reponses);
	}

	////// tebaa el Rating

	@PostMapping("/Rating/CreateRate/{idReponse}")
	public Rating CreateRating(@RequestBody Rating rating, @PathVariable Integer idReponse) {
		return IReclamationService.CreateRating(rating, idReponse);
	}
	@GetMapping("/Rating/GetRating/{idRating}")
	public Optional < Rating > findRatingById(@PathVariable Long idRating) {
		return IReclamationService.findRatingById(idRating);

	}

	@PutMapping("/Rating/UpdateRating/{idRating}")
	public ResponseEntity < Rating > UpdateRating(@RequestBody Rating rating, @PathVariable Long idRating) {
		Optional < Rating > ratingOptional = IReclamationService.findRatingById(idRating);
		if (ratingOptional.isPresent()) {
			rating.setIdRate(idRating);
			return ResponseEntity.ok(IReclamationService.UpdateRating(rating));
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/Rating/getRateForReponse/{idReponse}")
	public List < Rating > getRatingsForReponse(@PathVariable Integer idReponse) {
		Reponse reponse = reponseRepository.findById(idReponse).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return reponse.getRatings();
	}

	@GetMapping("/Rating/getAllRatingByValue/{value}")
	public List < Rating > GetAllRAtingsByValue(@PathVariable int value) {
		return IReclamationService.GetAllRAtingsByValue(value);
	}

	@PostMapping("/Reclamation/AddReclamation/{userName}")
	public ResponseEntity < Reclamation > AddReclamation(@RequestBody Reclamation reclamation, @PathVariable String userName) {
		try {
			reclamation.setComment(BadWordFilter.getCensoredText(reclamation.getComment()));
			Reclamation AddReclamation = IReclamationService.AddReclamation(reclamation, userName);
			return new ResponseEntity < > (AddReclamation, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add reclamation", e);
		}
	}

	@GetMapping("/Reclamation/getRecByUserName/{userName}")
	public ResponseEntity < List < Reclamation >> getReclamationByUserName(@PathVariable String userName) {
		List < Reclamation > reclamations = IReclamationService.getReclamationByUserName(userName);
		return new ResponseEntity < > (reclamations, HttpStatus.OK);
	}

	@GetMapping("/Reclamation/GenderReclamationsByGender/{gender}")
	public ResponseEntity < List < Reclamation >> getReclamationsByGender(@PathVariable("gender") String gender) {
		try {
			List < Reclamation > reclamations = IReclamationService.getReclamatinByGender(gender);
			return new ResponseEntity < > (reclamations, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unable to get reclamations by gender", e);
		}
	}

	@GetMapping("/Reclamation/getAllReclamation")
	public List < Reclamation > findAllRec(Reclamation reclamation) {
		return IReclamationService.findAllRec(reclamation);
	}

	@GetMapping("/Reclamation/GetReclamationById/{idRec}")
	public Optional < Reclamation > getReclamationById(@PathVariable Long idRec) {
		return IReclamationService.getReclamationById(idRec);
	}

	@PutMapping("/Reclamation/changerStatuReclamationTraitement/{admin}/{id}/{statu}")
	public Reclamation changerStatuReclamationTraitement(@PathVariable Long id, @PathVariable String admin, @PathVariable String statu) {
		return IReclamationService.changerStatuReclamationTraitement(admin, id, statu);
	}

	@GetMapping("/Reclamation/getReclamationVerifie")
	public List < Reclamation > getReclamationsVerifie() {
		return IReclamationService.getReclamationByStatus(Statu.VERIFIE.toString());
	}
	@GetMapping("/reclamations/encours")
	public List < Reclamation > getReclamationsEnCours() {
		return IReclamationService.getReclamationByStatus(Statu.EN_COURS.toString());
	}
	@GetMapping("/reclamations/nontraitees")
	public List < Reclamation > getReclamationsNonTraitee() {
		return IReclamationService.getReclamationByStatus(Statu.NON_TRAITE.toString());
	}

	@GetMapping("/reclamations/rejetees")
	public List < Reclamation > getReclamationsRejetee() {
		return IReclamationService.getReclamationByStatus(Statu.REJETEE.toString());
	}

	@GetMapping("/Reclamation/countByUserGender")
	public ResponseEntity < List < Object[] >> getReclamationCountByGender() {
		try {
			List < Object[] > result = IReclamationService.getReclamationCountByGender();
			return new ResponseEntity < > (result, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get reclamation count by user gender ", e);
		}
	}

	@GetMapping("/Reclamation/countByObjet")
	public ResponseEntity < List < Object[] >> getReclamationCountByObjet() {
		try {
			List < Object[] > result = IReclamationService.getReclamationCountByObjet();
			return new ResponseEntity < > (result, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get reclamation count by objet", e);
		}
	}

	@PostMapping("/Foyer/foyerAdd")
	 public void foyerAdd (@RequestBody Foyer foyer){

		IReclamationService.foyerAdd(foyer);

	}
	@GetMapping("/Foyer/GetAllFoyer")
	public List < Foyer > getAllFoyer() {

		return IReclamationService.getAllFoyer();
	}

	@PostMapping("/Chambre/chambreAdd/{id}")
	public Chambre chambreAdd(@RequestBody Chambre chambre,@PathVariable Long id){
		return IReclamationService.chambreAdd(chambre,id);
	}
	@PostMapping("/User/AddUser")
	public void AddUser(@RequestBody User user) {
		IReclamationService.AddUser(user);
	}
	@GetMapping("/Foyer/getFoyer/{id}")
	public Optional<Foyer> getFoyer(@PathVariable Long id){
		return IReclamationService.getFoyer(id);
	}

	@GetMapping("/Chambre/getChambre/{id}")
	public Optional<Chambre> getchambre(@PathVariable Long id){
		return IReclamationService.getchambre(id);
	}
	@PostMapping("/Chambre/affecterEtudiant/{username}")
	public Chambre affecterChambre (@PathVariable String username)
	{
		return IReclamationService.affectationEtudiantFoyerChambreSurDemande(username);
	}
	@DeleteMapping("/Foyer/deletefoyer/{idFoyer}")
	public ResponseEntity< Void >  deleteFoyerById(Long idfoyer){
		Optional < Foyer > optionalFoyer = IReclamationService.getFoyer(idfoyer);
		if (optionalFoyer.isPresent()) {
			IReclamationService.deleteFoyerById(idfoyer);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}



	/*	@GetMapping("/Reclamation/getRecByUserName/{userName}")
	public ResponseEntity < List < Reclamation >> getReclamationByUserName(@PathVariable String userName) {
		List < Reclamation > reclamations = Pi_Mobility.getReclamationByUserName(userName);
		return new ResponseEntity < > (reclamations, HttpStatus.OK);
	}*/

	@GetMapping("/Chambre/getChambreByUser/{userName}")
	public Chambre getChambreByUser(@PathVariable String userName){
		Chambre chambre= IReclamationService.getChambreByUser(userName);
		return chambre;
	}

	@PutMapping("/Chambre/removeEtudiantFromChambre/{chambreId}/{username}")
	public Chambre removeEtudiantFromChambre(@PathVariable Long chambreId,@PathVariable String username){
		return IReclamationService.removeEtudiantFromChambre(chambreId,username);
	}

	@PutMapping("/foyer/deleteChambreFromFoyer/{foyerId}/{chambreId}")
	public void removeChambreFromFoyer(@PathVariable Long foyerId,@PathVariable Long chambreId){
		 IReclamationService.removeChambreFromFoyer(foyerId,chambreId);
	}












}
