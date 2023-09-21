package tn.esprit.spring.services.Opportunity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.interfaces.Pi_Mobility;
import tn.esprit.spring.repositories.*;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class Pi_MobilityImplementation implements Pi_Mobility {
    @Autowired
    CondidacyRepository condidacyRepository;
    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    AnswerAttemptRepository answerAttemptRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuizAttemptRepository quizAttemptRepository;
    @Autowired
    ScheduelRepository scheduelRepository;

    @Override
    public List<Opportunity> findAllOpportunities() {
        return opportunityRepository.findAll();
    }

    @Override
    public Optional<Opportunity> findOpportunityById(Integer id) {
        return opportunityRepository.findById(id);
    }
    @Override
    public Opportunity createOpportunity(Opportunity opportunity, String Id_Partner) throws IOException, WriterException {
        String qrContent = opportunity.getId_Opportunity() + "_" + UUID.randomUUID().toString();
       // opportunity.setQrContent(qrContent);

        // Générer l'image du QR code
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // byte[] qrCodeImage = outputStream.toByteArray();
      //  opportunity.setQrCodeImage(qrCodeImage);
        User user = userRepository.findById(Id_Partner).orElse(null);
        opportunity.setCreatedBy(user);
        return opportunityRepository.save(opportunity);


    }

    @Override
    public Opportunity updateOpportunity(Opportunity opportunity) {
        // opportunity.setId_Opportunity()
        return opportunityRepository.save(opportunity);
    }

    @Override
    public void deleteOpportunityById(Integer id) {
        opportunityRepository.deleteById(id);
    }

    @Override
    public List<Condidacy> findAllCandidates() {
        return condidacyRepository.findAll();
    }

    @Override
    public Optional<Condidacy> findCandidateById(Integer id) {
        return condidacyRepository.findById(id);
    }

    @Override
    public Condidacy createCandidateAndAssignEtudiant(Condidacy condidacy, String Id_Student, int ID_Opportuinity) throws MessagingException, IOException {
        Schedule schedule = new Schedule();
        schedule.setStatus(0);
        schedule.setTypeScheduel(TypeScheduel.First_Selection);
        scheduelRepository.save(schedule);
        condidacy.setSchedule(schedule);
      //   schedule.setCandidates(sendSelectedCandidatesEmailsTest(condidacy.getOpportunity().getId_Opportunity()));
        condidacy.setStatus(status.In_Progress);
        User user = userRepository.findById(Id_Student).orElse(null);
        Opportunity opportunity = opportunityRepository.findById(ID_Opportuinity).orElse(null);
        condidacy.setUser(user);
        condidacy.setOpportunity(opportunity);
        opportunityRepository.save(opportunity);
        condidacy.setScore(0);
        return condidacyRepository.save(condidacy);
    }


    @Override
    public Condidacy updateCandidate(Condidacy condidacy) {
        return condidacyRepository.save(condidacy);
    }

    @Override
    public void deleteCandidateById(Integer id) {
        condidacyRepository.deleteById(id);
    }

    public String extraireChaine(String chaine) {
        int index_chiffre = -1;
        for (int i = 1; i < chaine.length(); i++) {
            if (Character.isDigit(chaine.charAt(i))) {
                index_chiffre = i;
                break;
            }
        }
        if (index_chiffre > 1) {
            return chaine.substring(1, index_chiffre);
        } else {
            return null;
        }
    }

    @Override
    public List<Opportunity> RetreiveOpportunitiesForStudentbySpecialite(String id_Student) {
        User Student = userRepository.findById(id_Student).orElse(null);

        String specialite = extraireChaine(Student.getGrade());
        // System.out.println(specialite);

        List<Opportunity> opportunities = new ArrayList<>();

        for (Opportunity op : opportunityRepository.findAll()) {
            String sp = String.valueOf(op.getSpecialite());
            if (sp.toUpperCase().equals(specialite.toUpperCase()) || sp.toUpperCase().equals("ALL")) {
                opportunities.add(op);
                System.out.println(op.getSpecialite());
            }
        }


        return opportunities;
    }

    @Override
    public List<Condidacy> RtreiveStudentCondidacies(String id_Student) {
        User student = userRepository.findById(id_Student).orElse(null);
        //  List<Condidacy> condidacies =student.getCandidacies();

        return student.getCandidacies();
    }

    @Override
    public Condidacy UpdateCondidacy(Integer id_Condidacy) {
        Condidacy condidacy = condidacyRepository.findById(id_Condidacy).orElse(null);
        condidacy.setStatus(status.Accepted);

        return condidacyRepository.save(condidacy);
    }


    public String UpdateCondidacy(Condidacy condidacy) {

        condidacy.setStatus(status.Accepted);
        return condidacy.getUser().getUserName();
    }

    public List<Condidacy> trierEtudiantsParScore(Integer Id_Opportunity) {

        List<Condidacy> ToSort = new ArrayList<>();
        Opportunity opportunity = opportunityRepository.findById(Id_Opportunity).orElse(null);
        for (Condidacy condidacy : condidacyRepository.findAll()) {
            if (condidacy.getOpportunity().getId_Opportunity() == Id_Opportunity) {
                float coefTotal = opportunity.getCoef1stYear() + opportunity.getCoef2stYear() + opportunity.getCoef3stYear();
                condidacy.setScore((condidacy.getMoyenne_1year() * opportunity.getCoef1stYear() + condidacy.getMoyenne_2year() * opportunity.getCoef2stYear() + condidacy.getMoyenne_3year() * opportunity.getCoef3stYear()) / coefTotal);
                condidacyRepository.save(condidacy);
                ToSort.add(condidacy);
            }
        }
        // Trier la liste des étudiants pour l'opportunité donnée par ordre décroissant de score
        ToSort.sort(Comparator.comparing(Condidacy::getScore).reversed());
        return ToSort;
    }

    @Override
    public List<Condidacy> CalculScore(Integer Id_Opportunity) {
        Opportunity opportunity = opportunityRepository.findById(Id_Opportunity).orElse(null);
        for (Condidacy condidacy : condidacyRepository.findAll()) {
            if (condidacy.getOpportunity().getId_Opportunity() == Id_Opportunity) {
                condidacy.setScore((condidacy.getMoyenne_1year() + condidacy.getMoyenne_2year() * 2 + condidacy.getMoyenne_3year() * 3) / 6);
                condidacyRepository.save(condidacy);
            }
        }
        return condidacyRepository.findAll();
    }

    public void sendEmailToTopNCandidates(int n, int opportunityId) throws MessagingException {
        List<Condidacy> sortedCondidacyList = getTopNCandidatures(opportunityId);

        for (int i = 0; i < Math.min(n, sortedCondidacyList.size()); i++) {
            Condidacy condidacy = sortedCondidacyList.get(i);

            // Envoyer un e-mail à l'étudiant avec le score et le résultat de sa candidature
            String to = condidacy.getUser().getEmail();
            String subject = "Résultat de votre candidature à l'opportunité " + opportunityId;
            String message = "Bonjour " + condidacy.getUser().getUserFirstName() + ",\n\n"
                    + "Nous sommes heureux de vous informer que votre candidature pour l'opportunité " + opportunityId
                    + " a été acceptée avec un score de " + condidacy.getScore() + ".\n\n"
                    + "Félicitations !\n\n"
                    + "Cordialement,\n"
                    + "L'équipe de recrutement";

            // Envoyer l'e-mail en utilisant JavaMail
            MimeMessage email = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);
            javaMailSender.send(email);
        }
       /* SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("mariemaljene0@gmail.com");
        message.setSubject("test");
       */
    }

    public List<Condidacy> getTopNCandidatures(Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findById(opportunityId).orElse(null);
        int n = opportunity.getCapacity();
        List<Condidacy> CondidacyTOP = trierEtudiantsParScore(opportunityId);
        List<Condidacy> topNCandidatures = new ArrayList<>();
        List<Condidacy> ListeAttente = new ArrayList<>();
        if (opportunity.getNeeds() == 0) {
            for (int i = 0; i < n && i < CondidacyTOP.size(); i++) {
                topNCandidatures.add(CondidacyTOP.get(i));
                CondidacyTOP.get(i).setStatus(status.Pre_Selected);
                userRepository.save(CondidacyTOP.get(i).getUser());


            }
        } else {
            int count = 0;
            for (Condidacy candidacy : CondidacyTOP) {
                if (candidacy.getScore() >= opportunity.getNeeds()) {
                    topNCandidatures.add(candidacy);
                    count++;
                    if (count == n) {
                        break;
                    }
                    candidacy.setStatus(status.Accepted);
                    userRepository.save(candidacy.getUser());

                } else candidacy.setStatus(status.Refused);
                userRepository.save(candidacy.getUser());
            }
        }


        return topNCandidatures;
    }

    public List<Condidacy> GetListeAttente(Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findById(opportunityId).orElse(null);
        int n = opportunity.getCapacity();
        List<Condidacy> CondidacyTOP = trierEtudiantsParScore(opportunityId);
        List<Condidacy> ListeAttente = new ArrayList<>();

        for (int i = n; i < n + n && i < CondidacyTOP.size(); i++) {
            ListeAttente.add(CondidacyTOP.get(i));
            CondidacyTOP.get(i).setStatus(status.Pre_Selected);
            userRepository.save(CondidacyTOP.get(i).getUser());


        }


        return ListeAttente;
    }

    public void sendSelectedCandidatesEmails(Integer opportunityId) {
        List<Condidacy> selectedCandidates = getTopNCandidatures(opportunityId);

        for (Condidacy candidate : selectedCandidates) {
            // Créer un objet `SimpleMailMessage` pour l'email à envoyer
            SimpleMailMessage message = new SimpleMailMessage();

            // Définir le destinataire et le sujet de l'email
            message.setTo(candidate.getUser().getEmail());
            message.setSubject("Opportunity selection");

            // Définir le corps du message de l'e-mail
            message.setText("Dear " + candidate.getUser().getUserName() + ",\n\n" +
                    "Congratulations! You have been selected for the following opportunity:\n\n" +
                    opportunityRepository.findById(opportunityId).get().getTitle() + "\n\n" +
                    "Please contact the opportunity provider for further details.\n\n" +
                    "Best regards,\n" +
                    "Your Application Team");

            // Envoyer l'email
            javaMailSender.send(message);
        }

    }

    public List<Condidacy> sendSelectedCandidatesEmailsTest(Integer opportunityId) throws MessagingException, IOException {
        List<Condidacy> selectedCandidates = getTopNCandidatures(opportunityId);
        //   List<Quiz > quizList =opportunityRepository.findById(opportunityId).get().getQuizzes();
        List<Condidacy> ListeAttente = GetListeAttente(opportunityId);

        for (Condidacy candidate : selectedCandidates) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(candidate.getUser().getEmail());
            helper.setSubject("Opportunity selection" + opportunityRepository.findById(opportunityId).get().getTitle());

            // Set the HTML content of the email
            String htmlContent = "<html><body>"
                    + "<p>Dear " + candidate.getUser().getUserName() + ",</p>"
                    + "<p>Congratulations! You have been selected for the following opportunity:</p>"
                    + "<h1>" + opportunityRepository.findById(opportunityId).get().getTitle() + "</h1>"
                    + "<h2>" + opportunityRepository.findById(opportunityId).get().getCreatedBy().getUserName() + "</h2>"
                    + "<p> A quiz will be available on the platform </p>" + opportunityRepository.findById(opportunityId).get().getQuizzesQuiz().getStartDate() + "</p>"
                    + "<p>Until  </p>" + opportunityRepository.findById(opportunityId).get().getQuizzesQuiz().getEndDate() + "</p>"
                    + "<p>Please contact the opportunity provider for further details.</p>"
                    + "<img src='cid:image'>"
                    + "<p>Best regards,<br>Your Application Team</p>"
                    + "</body></html>";

            helper.setText(htmlContent, true);

            // Add an image as an attachment
            ClassPathResource imageResource = new ClassPathResource("img.png");
            InputStream inputStream = imageResource.getInputStream();
            DataSource imageDataSource = new ByteArrayDataSource(inputStream, "image/png");
            helper.addInline("image", imageDataSource);

            javaMailSender.send(message);
        }
        ///Liste attente
        for (Condidacy candidate : ListeAttente) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(candidate.getUser().getEmail());
            helper.setSubject("Opportunity selection" + opportunityRepository.findById(opportunityId).get().getTitle());

            // Set the HTML content of the email
            String htmlContent = "<html><body>"
                    + "<p>Dear " + candidate.getUser().getUserName() + ",</p>"
                    + "<p>You are in a temporary list of :</p>"
                    + "<h1>" + opportunityRepository.findById(opportunityId).get().getTitle() + "</h1>"
                    + "<h2>" + opportunityRepository.findById(opportunityId).get().getCreatedBy().getUserName() + "</h2>"
                    + "<p>You can Be called a tout moment.</p>"

                    + "<p>Please contact the opportunity provider for further details.</p>"
                    + "<img src='cid:image'>"
                    + "<p>Best regards,<br>Your Application Team</p>"
                    + "</body></html>";

            helper.setText(htmlContent, true);

            // Add an image as an attachment
            ClassPathResource imageResource = new ClassPathResource("img.png");
            InputStream inputStream = imageResource.getInputStream();
            DataSource imageDataSource = new ByteArrayDataSource(inputStream, "image/png");
            helper.addInline("image", imageDataSource);

            javaMailSender.send(message);
        }
        return selectedCandidates;
    }


    @Override
    public void ajouterQuizAvecQuestionsEtReponses(Quiz quiz) {

    }

    @Transactional

    @Override
    public void ajouterQuestion(Question question, Integer quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new IllegalArgumentException("Le quiz avec l'ID " + quizId + " n'existe pas."));
        // Ajouter la question au quiz
        quiz.getQuestions().add(question);

        // Ajouter le quiz à la question
        question.getQuizzes().add(quiz);

        questionRepository.save(question);

    }

    @Transactional

    @Override
    public void ajouterReponse(Integer questionId, Answer reponse) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("La question avec l'ID " + questionId + " n'existe pas."));
        reponse.setQuestion(question);
        answerRepository.save(reponse);

    }

    @Override
    public void ajouterQuiz(Quiz quiz, Integer OpportunityId) {

    }

    @Transactional
    public void ajouterAnswer(Answer answer, Integer questionId) {
        // Récupérer la question correspondante en base de données
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question introuvable avec l'id " + questionId));

        // Associer la réponse à la question
        answer.setQuestion(question);

        // Enregistrer la réponse en base de données
        answerRepository.save(answer);
    }


    public ResponseEntity<Question> getQuestionById(Integer questionId) {

        Optional<Question> question = Optional.ofNullable(questionRepository.findById(questionId).orElse(null));
        if (question.isPresent()) {
            return ResponseEntity.ok().body(question.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public void sendOpportunitySelectionEmailsOnDeadline(Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findById(opportunityId).orElse(null);
        if (opportunity == null) {
            return;
        }
        LocalDate deadline = opportunity.getEndDate();
        if (deadline == null) {
            return;
        }
        LocalDateTime midnightDeadline = deadline.atTime(LocalTime.MIDNIGHT);
        Date date = Date.from(midnightDeadline.atZone(ZoneId.systemDefault()).toInstant());

        List<Condidacy> selectedCandidates = getTopNCandidatures(opportunityId);

        for (Condidacy candidate : selectedCandidates) {
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(candidate.getUser().getEmail());
                helper.setSubject("Opportunity selection");

                // Set the HTML content of the email
                String htmlContent = "<html><body>"
                        + "<p>Dear " + candidate.getUser().getUserName() + ",</p>"
                        + "<p>Congratulations! You have been selected for the following opportunity:</p>"
                        + "<h1>" + opportunity.getTitle() + "</h1>"
                        + "<h2>" + opportunity.getCreatedBy().getUserName() + "</h2>"
                        + "<p>Please contact the opportunity provider for further details.</p>"
                        + "<img src='cid:image'>"
                        + "<p>Best regards,<br>Your Application Team</p>"
                        + "</body></html>";

                helper.setText(htmlContent, true);

                // Add an image as an attachment
                ClassPathResource imageResource = new ClassPathResource("img.png");
                InputStream inputStream = imageResource.getInputStream();
                DataSource imageDataSource = new ByteArrayDataSource(inputStream, "image/png");
                helper.addInline("image", imageDataSource);

                javaMailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
   /* public void ajouterQuizAvecQuestionsEtReponses(Quiz quiz) {
        // Enregistrer le quiz
        Quiz savedQuiz = quizRepository.save(quiz);

        // Ajouter les questions au quiz
        for (Question question : quiz.getQuestions()) {
            question.setQuiz(savedQuiz);
            Question savedQuestion = questionRepository.save(question);

            // Ajouter les réponses à la question
            for (Answer reponse : question.getAnswers()) {
                reponse.setQuestion(savedQuestion);
                answerRepository.save(reponse);
            }
        }
    }
*/

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void ajouterQuiz(Quiz quiz, List<Question> questions, Integer Id_Opportunity) {
        Opportunity opportunity = opportunityRepository.findById(Id_Opportunity).orElse(null);

        for (Question question : questions) {
            question.getQuizzes().add(quiz);
        }

        quiz.setOpportunity(opportunity);
        quiz.setQuestions((Set<Question>) questions);

        quizRepository.save(quiz);
    }


    public QuizAttempt startQuizAttempt(Condidacy candidacy, Integer quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setCondidacy(candidacy);
        quizAttempt.setQuiz(quiz);
        quizAttemptRepository.save(quizAttempt);
        return quizAttempt;
    }

    public void submitAnswerAttempt(Integer quizAttemptId, Integer questionId, Integer answerId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId)
                .orElseThrow(() -> new RuntimeException("Quiz Attempt not found"));
        Question question = quizAttempt.getQuiz().getQuestions().stream()
                .filter(q -> q.getIdQuestion().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question not found in the quiz"));

        Answer answer = question.getAnswers().stream()
                .filter(a -> a.getId_Answer().equals(answerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Answer not found for the question"));

        AnswerAttempt answerAttempt = new AnswerAttempt();
        answerAttempt.setAnswer(answer);
        answerAttempt.setQuizAttempt(quizAttempt);
        answerAttemptRepository.save(answerAttempt);
    }

    public int finishQuizAttempt(Integer quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId)
                .orElseThrow(() -> new RuntimeException("Quiz Attempt not found"));

        int score = quizAttempt.getAnswerAttempts().stream()
                .filter(a -> a.getAnswer().isCorrect())
                .mapToInt(a -> 1)
                .sum();

        quizAttempt.setScore(score);
        quizAttemptRepository.save(quizAttempt);

        return score;
    }

    public List<Condidacy> trierEtudiantsParScoreQuiz(Integer Id_Opportunity) {
        Opportunity opportunity = opportunityRepository.findById(Id_Opportunity).orElse(null);

        // Récupérer toutes les candidatures pour cette opportunité
        //  List<Condidacy> condidacies = condidacyRepository.findById(Id_Opportunity).orElse(null);
        List<Condidacy> condidaciesAll = condidacyRepository.findAll();
        List<Condidacy> condidacies = new ArrayList<>();
        for (Condidacy condidacy : condidaciesAll) {
            if (condidacy.getOpportunity() == opportunity) {
                condidacies.add(condidacy);
            }
        }

        QuizAttempt quizAttempt1 = null;
        for (QuizAttempt quizAttempt2 : quizAttemptRepository.findAll()) {
            if (quizAttempt2.getQuiz().getId_Quiz() == opportunity.getQuizzesQuiz().getId_Quiz() && quizAttempt2.getCondidacy().getOpportunity().getId_Opportunity() == Id_Opportunity) {
                quizAttempt1 = quizAttempt2;
            }
        }
        // Trier les candidatures par score de QuizAttempt
        QuizAttempt finalQuizAttempt = quizAttempt1;
        condidacies.sort(Comparator.comparing(condidacy -> {

            QuizAttempt quizAttempt = finalQuizAttempt;
            return quizAttempt.getScore();
        }).reversed());

        return condidacies;
    }

    public List<Condidacy> sendSelectedCandidatesEmailsQuiz(Integer opportunityId) throws MessagingException, IOException {
        List<Condidacy> selectedCandidates = trierEtudiantsParScoreQuiz(opportunityId);
        //   List<Quiz > quizList =opportunityRepository.findById(opportunityId).get().getQuizzes();
        //  List<Condidacy> ListeAttente= GetListeAttente(opportunityId);

        for (Condidacy candidate : selectedCandidates) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(candidate.getUser().getEmail());
            helper.setSubject("Opportunity selection" + opportunityRepository.findById(opportunityId).get().getTitle());
            candidate.setStatus(status.Accepted);
            condidacyRepository.save(candidate);
            // Set the HTML content of the email
            String htmlContent = "<html><body>"
                    + "<p>Dear " + candidate.getUser().getUserName() + ",</p>"
                    + "<p>Congratulations! You have been  Finally Accepted for the following opportunity:</p>"
                    + "<h1>" + opportunityRepository.findById(opportunityId).get().getTitle() + "</h1>"
                    + "<h2>" + opportunityRepository.findById(opportunityId).get().getCreatedBy().getUserName() + "</h2>"
                    + "<p>Please contact the opportunity provider for further details.</p>"
                    + "<img src='cid:image'>"
                    + "<p>Best regards,<br>Your Application Team</p>"
                    + "</body></html>";

            helper.setText(htmlContent, true);

            // Add an image as an attachment
            ClassPathResource imageResource = new ClassPathResource("img.png");
            InputStream inputStream = imageResource.getInputStream();
            DataSource imageDataSource = new ByteArrayDataSource(inputStream, "image/png");
            helper.addInline("image", imageDataSource);

            javaMailSender.send(message);
        }
        return selectedCandidates;
    }

    @Override
    public List<Question> RtreiveQuestionOfQuizBySpeciality(String UserName, Integer id_Quiz) {
        List<Question> questions = new ArrayList<>();
        User user = userRepository.findById(UserName).orElse(null);
        Quiz quiz = quizRepository.findById(id_Quiz).orElse(null);

        for (Question question : quiz.getQuestions()) {
            String specialy = extraireChaine(user.getGrade());
            questions = AfficherQuestionParspecialite(specialy);
            questions.add(question);
        }
        return questions;
    }


    List<Question> AfficherQuestionParspecialite(String speciality) {
        List<Question> questions = new ArrayList<>();
        for (Question question : questionRepository.findAll()) {
            if (question.getSpecialty().equals(speciality)) {
                questions.add(question);
            }
        }
        return questions;
    }

    private String generateQRContent(Opportunity opportunity) {
        return "https://example.com/opportunity/" + opportunity.getId_Opportunity();
    }

    public Map<String, Double> getAvgQuizScoreByOpportunityType() {
        List<Object[]> resultList = quizAttemptRepository.findAvgQuizScoreByOpportunityType();

        Map<String, Double> resultMap = new HashMap<>();

        for (Object[] result : resultList) {
            String opportunityType = (String) result[0];
            Double avgQuizScore = (Double) result[1];

            resultMap.put(opportunityType, avgQuizScore);
        }

        return resultMap;
    }

}
