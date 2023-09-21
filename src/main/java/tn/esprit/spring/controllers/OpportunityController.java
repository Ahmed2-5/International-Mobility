package tn.esprit.spring.controllers;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Opportunity;
import tn.esprit.spring.interfaces.Pi_Mobility;
import tn.esprit.spring.interfaces.QrCodeInterface;
import tn.esprit.spring.repositories.*;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Opportunity")
public class OpportunityController {
    @Autowired
    QrCodeInterface qrCodeInterface;
    @Autowired
    Pi_Mobility pi_mobility;
    @Autowired
    QuizAttemptRepository quizAttemptRepository;
    @Autowired
    AnswerAttemptRepository answerAttemptRepository;
    @Autowired
    CondidacyRepository condidacyRepository;
    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepositoy;
    @GetMapping("/Opportunity/GetAll")
    public List<Opportunity> getAllOpportunities() {
        return pi_mobility.findAllOpportunities();
    }
    @PermitAll
    @GetMapping("/Opportunity/GetById/{id}")
    public ResponseEntity<Opportunity> getOpportunityById(@PathVariable Integer id) {
        Optional<Opportunity> opportunity = pi_mobility.findOpportunityById(id);
        if (opportunity.isPresent()) {
            return ResponseEntity.ok(opportunity.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/Opportunity/CreateNewOpportunity/{id_Partner}")
    public Opportunity createOpportunity(@RequestBody Opportunity opportunity, @PathVariable String id_Partner) throws IOException, WriterException {
        System.out.println(Paths.get("qr_code_template.html").toAbsolutePath().toString());

        return pi_mobility.createOpportunity(opportunity, id_Partner);
    }

    @PutMapping("/Opportunity/Update/{id}")
    public ResponseEntity<Opportunity> updateOpportunity(@PathVariable Integer id, @RequestBody Opportunity opportunity) {
        Optional<Opportunity> existingOpportunity = pi_mobility.findOpportunityById(id);
        if (existingOpportunity.isPresent()) {
            opportunity.setId_Opportunity(id);
            return ResponseEntity.ok(pi_mobility.updateOpportunity(opportunity));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/Opportunity/Delete/{id}")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Integer id) {
        Optional<Opportunity> existingOpportunity = pi_mobility.findOpportunityById(id);
        if (existingOpportunity.isPresent()) {
            pi_mobility.deleteOpportunityById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("generateQRCodeForOpportunity/{id-opportunity}")
    public ResponseEntity<byte[]> generateQRCodeForOpportunity(@PathVariable("id-opportunity") Integer idOpportunity) {
        try {
            byte[] qrCode = qrCodeInterface.generateQRCodeForOpportunity(idOpportunity);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/opportunities/{id}")
    public String showOpportunityDetails(@PathVariable Integer id, Model model) {
        Opportunity opportunity = opportunityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Opportunity not found with id: " + id));
        model.addAttribute("opportunity", opportunity);
        return "opportunity-details";
    }
}
