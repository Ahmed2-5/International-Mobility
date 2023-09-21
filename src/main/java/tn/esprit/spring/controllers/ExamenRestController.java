package tn.esprit.spring.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.interfaces.Pi_Mobility;
import tn.esprit.spring.interfaces.StatInterface;
import tn.esprit.spring.repositories.*;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@RestController
@RequestMapping("/Pi_Mobility")
public class ExamenRestController {
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
    public Opportunity createOpportunity(@RequestBody Opportunity opportunity, @PathVariable String id_Partner) throws IOException, WriterException, WriterException {
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

    @GetMapping(value = "/opportunities/{id}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@PathVariable("id") Integer id) throws WriterException, IOException {
        Opportunity opportunity = opportunityRepository.findById(id).orElse(null);
        if (opportunity == null) {
            return ResponseEntity.notFound().build();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(generateQRCodeContent(opportunity), BarcodeFormat.QR_CODE, 350, 350);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(stream.toByteArray(), headers, HttpStatus.OK);
    }

    private String generateQRCodeContent(Opportunity opportunity) {
        StringBuilder builder = new StringBuilder();
        builder.append("Title: ").append(opportunity.getTitle()).append("\n");
        builder.append("Description: ").append(opportunity.getDescription()).append("\n");
        builder.append("End Date: ").append(opportunity.getEndDate()).append("\n");
        return builder.toString();
    }



    ///////////////////   Condidacy
    @GetMapping("/Condidacy/GetAllCondidacies")
    public List<Condidacy> getAllCandidates() {
        return pi_mobility.findAllCandidates();
    }

    @GetMapping("/Condidacy/GetCondidcayById/{id}")
    public ResponseEntity<Condidacy> getCandidateById(@PathVariable Integer id) {
        Optional<Condidacy> candidate = pi_mobility.findCandidateById(id);
        if (candidate.isPresent()) {
            return ResponseEntity.ok(candidate.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseBody
    @GetMapping("/Condidacy/GetOpportunitySpecialite/{id}")
    public List<Opportunity> GetOpportunitySpecialite(@PathVariable String id) {
        return pi_mobility.RetreiveOpportunitiesForStudentbySpecialite(id);

    }

    @PostMapping("/Condidacy/CreateNew/{id_Student}/{id_Opportunity}")
    public Condidacy createCandidate(@RequestBody Condidacy candidate, @PathVariable String id_Student, @PathVariable int id_Opportunity) throws MessagingException, IOException {
        return pi_mobility.createCandidateAndAssignEtudiant(candidate, id_Student, id_Opportunity);
    }

    @PutMapping("/Condidacy/UpdateCondidacy/{id}")
    public ResponseEntity<Condidacy> updateCandidate(@PathVariable Integer id, @RequestBody Condidacy candidate) {
        Optional<Condidacy> existingCandidate = pi_mobility.findCandidateById(id);
        if (existingCandidate.isPresent()) {
            candidate.setId_Condidacy(id);
            return ResponseEntity.ok(pi_mobility.updateCandidate(candidate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/Condidacy/DeleteCondidacy/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Integer id) {
        Optional<Condidacy> existingCandidate = pi_mobility.findCandidateById(id);
        if (existingCandidate.isPresent()) {
            pi_mobility.deleteCandidateById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseBody
    @GetMapping("/Condidacy/GetCondidaciesOfStudent/{id}")
    public List<Condidacy> GetCondidaciesOfStudent(@PathVariable String id) {
        return pi_mobility.RtreiveStudentCondidacies(id);

    }

    @ResponseBody
    @GetMapping("/Condidacy/UpdateStatus/{id}")
    public Condidacy UpdateStatus(@PathVariable Integer id) {
        return pi_mobility.UpdateCondidacy(id);

    }

    @ResponseBody
    @GetMapping("/CalculScore/{opportuniteId}")
    public List<Condidacy> CalculScore(@PathVariable Integer opportuniteId) {
        return pi_mobility.CalculScore(opportuniteId);

    }

    @ResponseBody
    @GetMapping("/Trie/{opportuniteId}")
    public List<Condidacy> Trie(@PathVariable Integer opportuniteId) {
        return pi_mobility.trierEtudiantsParScore(opportuniteId);

    }

    @GetMapping("candidatures/top/{opportunityId}")
    public List<Condidacy> getTopNCandidatures(@PathVariable Integer opportunityId) {


        return pi_mobility.getTopNCandidatures(opportunityId);
    }

    @PostMapping("Condidacy/sendSelectedCandidatesEmailsTest/{id_Opportunity}")
    public ResponseEntity<String> sendSelectedCandidatesEmailsTest(@PathVariable int id_Opportunity) {
        int n = 0;
        List<Condidacy> condidacy = condidacyRepository.findAll();
        for (Condidacy condidacy1 : condidacy) {
            if (condidacy1.getOpportunity().getId_Opportunity() == id_Opportunity) {
                n++;
            }
        }
        try {
            pi_mobility.sendSelectedCandidatesEmailsTest(id_Opportunity);
            return ResponseEntity.ok("Emails sent successfully to the top " + n + " candidates.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending emails: " + e.getMessage());
        }
    }

    @PostMapping("/Quiz/AddQuizQuestionAndResponse/{questionIds}/{Id_Opportunity}")
    public ResponseEntity<?> ajouterQuiz(@RequestBody Quiz quiz, @PathVariable List<Integer> questionIds, @PathVariable Integer Id_Opportunity) {
        HashSet<Question> questions = new HashSet<>();
        for (Integer questionId : questionIds) {
            {
                Question question = questionRepository.findById(questionId).orElse(null);
                questionRepository.save(question);

                questions.add(question);
            }
        }
        quiz.setQuestions((Set<Question>) questions);
        quiz.setQuestions(questions);
        int nb = questions.size();
        quiz.setNbQuestion(nb);
        Opportunity opportunity = opportunityRepository.findById(Id_Opportunity).orElse(null);
        quiz.setOpportunity(opportunity);
        pi_mobility.ajouterQuiz(quiz, Id_Opportunity);
        quizRepository.save(quiz);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idQuiz}/question/{Id_user}")
    public ResponseEntity<?> ajouterQuestionAuQuiz(@RequestBody Question question, String Id_user) {
        User user = userRepositoy.findById(Id_user).orElse(null);
        question.setUserQ(user);
        for (Answer answer : question.getAnswers()) {
            answer.setQuestion(question);
        }

        questionRepository.save(question);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/question/{idQuestion}/reponse")
    public ResponseEntity<?> ajouterReponseALaQuestion(@PathVariable Integer idQuestion, @RequestBody Answer reponse) {

        pi_mobility.ajouterReponse(idQuestion, reponse);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/startQuiz/{id_Quiz}/{id_Condidacy}")
    public ResponseEntity<String> startQuizAttempt(@RequestBody QuizAttempt quizAttempt, @PathVariable Integer id_Quiz, @PathVariable Integer id_Condidacy) {
        QuizAttempt savedQuizAttempt = quizAttemptRepository.save(quizAttempt);
        Quiz quiz = quizRepository.findById(id_Quiz).orElse(null);
        Condidacy condidacy = condidacyRepository.findById(id_Condidacy).orElse(null);
        savedQuizAttempt.setCondidacy(condidacy);
        savedQuizAttempt.getCondidacy().setId_Condidacy(id_Condidacy);
        savedQuizAttempt.setQuiz(quiz);
        savedQuizAttempt.setStartTime(LocalDate.now());
        LocalDateTime endTime = savedQuizAttempt.getStartTime().atTime(quiz.getDuration(), 0);
        savedQuizAttempt.setEndTime(endTime);
        quizRepository.save(quiz);
        //savedQuizAttempt.getCondidacy().setAttempted(true);
        return ResponseEntity.ok("QuizAttempt started with id: " + savedQuizAttempt.getIdQuizAttempt());
    }

    @PostMapping("/{quizAttemptId}/answer/{id_answer}")
    public ResponseEntity<String> answerQuestion(@PathVariable Integer quizAttemptId, @RequestBody AnswerAttempt answerAttempt, @PathVariable Integer id_answer) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElse(null);
        Answer answer = answerRepository.findById(id_answer).orElse(null);
        answerAttempt.setAnswer(answer);
        answerAttempt.setQuizAttempt(quizAttempt);
        answerAttemptRepository.save(answerAttempt);
        return ResponseEntity.ok("Answer saved successfully for QuizAttempt with id: " + quizAttemptId);
    }

    @GetMapping("/{quizAttemptId}/submit/{Id_condidacy}")
    public ResponseEntity<String> submitQuizAttempt(@PathVariable Integer quizAttemptId, @PathVariable Integer Id_condidacy) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElse(null);
        float i = calculateScore(quizAttempt, Id_condidacy);
        quizAttempt.setScore(calculateScore(quizAttempt, Id_condidacy));
        quizAttempt.getCondidacy().setAttempted(true);
        quizAttemptRepository.save(quizAttempt);
        return ResponseEntity.ok("QuizAttempt with id: " + quizAttemptId + " submitted successfully with score: " + quizAttempt.getScore());
    }

    private float calculateScore(QuizAttempt quizAttempt, int id_condidacy) {
        // List<Quiz> quizzes = quizRepository.findAll();
        Quiz quiz = quizAttempt.getQuiz();
        Condidacy condidacy = condidacyRepository.findById(id_condidacy).orElse(null);
        if (condidacy.getId_Condidacy() == quizAttempt.getCondidacy().getId_Condidacy()) {
            for (AnswerAttempt answerAttempt : quizAttempt.getAnswerAttempts()) {
                if (answerAttempt.getAnswer().isCorrect()) {
                    float i = answerAttempt.getQuizAttempt().getScore();
                    i += answerAttempt.getAnswer().getQuestion().getPoint();
                    answerAttempt.getQuizAttempt().setScore(i);
                    answerAttemptRepository.save(answerAttempt);
                    System.out.println(i);
                }
            }
        }

        System.out.println(quizAttempt.getScore() / quiz.getNbQuestion());
        return quizAttempt.getScore() / quiz.getNbQuestion();

    }

    // @Scheduled(fixedRate = 1000) // Check every second
    public void checkQuizTimes() {
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();

        for (QuizAttempt quizAttempt : quizAttempts) {
            // Check if end time of quiz has been reached
            if (quizAttempt.getEndTime() != null && currentTime.isAfter(quizAttempt.getEndTime())) {
                // Submit quiz attempt
                float score = calculateScore(quizAttempt, quizAttempt.getCondidacy().getId_Condidacy());
                quizAttempt.setScore(score);
                quizAttempt.getCondidacy().setAttempted(true);
                quizAttemptRepository.save(quizAttempt);
            }
        }
    }

    @PostMapping("Condidacy/sendSelectedCandidatesEmailQuiz/{id_Opportunity}")
    public ResponseEntity<String> sendSelectedCandidatesEmailsQuiz(@PathVariable int id_Opportunity) {
        int n = 0;
        List<Condidacy> condidacy = condidacyRepository.findAll();
        for (Condidacy condidacy1 : condidacy) {
            if (condidacy1.getOpportunity().getId_Opportunity() == id_Opportunity) {
                n++;
            }
        }
        try {
            pi_mobility.sendSelectedCandidatesEmailsQuiz(id_Opportunity);
            return ResponseEntity.ok("Emails sent successfully to the top " + n + " candidates.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending emails: " + e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/QuizParSpecialy/Quiz/{UserName}/{Id_Quiz}")
    public List<Question> GetQuizSpecialite(@PathVariable String id, @PathVariable Integer Id_Quiz) {
        return pi_mobility.RtreiveQuestionOfQuizBySpeciality(id, Id_Quiz);

    }

    public String saveImage(MultipartFile imageFile) throws IOException {
        String imageName = imageFile.getOriginalFilename();
        byte[] bytes = imageFile.getBytes();
        Path path = Paths.get("/uploadsFiles/" + imageName);
        Files.write(path, bytes);
        return imageName;
    }
    @Autowired
    StatInterface statInterface;
    @GetMapping("/specialityPercentage")
    public Map<String, Double> getOpportunitiesPercentageBySpeciality() {
        return statInterface.getOpportunitiesPercentageBySpeciality();
    }
    @GetMapping("/averageQuizCompletionTime")
    public Map<String, Double> getAverageQuizCompletionTime() {
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findAll();
        Map<String, Double> avgCompletionTimeByQuiz = new HashMap<>();

        for (QuizAttempt quizAttempt : quizAttempts) {
            Integer quizId = quizAttempt.getQuiz().getId_Quiz();
            LocalDateTime endTime = quizAttempt.getEndTime();
            LocalDate startDate = quizAttempt.getStartTime();
            long quizCompletionTimeSeconds = ChronoUnit.SECONDS.between(startDate.atStartOfDay(), endTime);
            double quizCompletionTimeMinutes = quizCompletionTimeSeconds / 60.0;

            if (avgCompletionTimeByQuiz.containsKey(quizId.toString())) {
                double currentAvgCompletionTime = avgCompletionTimeByQuiz.get(quizId.toString());
                double newAvgCompletionTime = (currentAvgCompletionTime + quizCompletionTimeMinutes) / 2.0;
                avgCompletionTimeByQuiz.put(quizId.toString(), newAvgCompletionTime);
            } else {
                avgCompletionTimeByQuiz.put(quizId.toString(), quizCompletionTimeMinutes);
            }
        }

        return avgCompletionTimeByQuiz;
    }






}