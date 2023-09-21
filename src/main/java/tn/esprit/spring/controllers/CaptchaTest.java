package tn.esprit.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import tn.esprit.spring.services.Opportunity.CaptchaImplementation;
import tn.esprit.spring.services.Opportunity.Pi_MobilityImplementation;


import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/CaptchaTry")
public class CaptchaTest {
    @Autowired
    QuizAttemptRepository quizAttemptRepository;
    @Autowired
    CaptchaRepository captchaRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    CondidacyRepository condidacyRepository;
    @Autowired
    CaptchaImplementation captchaImplementation;
    @Autowired
    ScheduelRepository scheduelRepository;
    @Autowired
    Pi_MobilityImplementation pi_mobilityImplementation;

    @PostMapping("/startQuiz/{id_Quiz}/{id_Condidacy}")
    public ResponseEntity<QuizAttempt> createQuizAttempt(@RequestBody QuizAttempt quizAttempt, @RequestParam String captcha, @PathVariable Integer id_Quiz, @PathVariable Integer id_Condidacy) throws MessagingException, IOException {

        String generatedCaptcha = captchaImplementation.getRandomCaptcha();

        if (!generatedCaptcha.equals(captcha)) {
            return ResponseEntity.badRequest().build();
        }

        QuizAttempt savedQuizAttempt = quizAttemptRepository.save(quizAttempt);
        Quiz quiz = quizRepository.findById(id_Quiz).orElse(null);
        Condidacy condidacy = condidacyRepository.findById(id_Condidacy).orElse(null);
        savedQuizAttempt.setCondidacy(condidacy);
        savedQuizAttempt.getCondidacy().setId_Condidacy(id_Condidacy);
        savedQuizAttempt.setQuiz(quiz);
        savedQuizAttempt.setStartTime(LocalDate.now());
        LocalDateTime endTime = savedQuizAttempt.getStartTime().atTime(quiz.getDuration(), 0);
        savedQuizAttempt.setEndTime(endTime);
        Schedule schedule = new Schedule();
        schedule.setStatus(0);
        schedule.setTypeScheduel(TypeScheduel.Second_Selection);
      //  schedule.setCandidates(pi_mobilityImplementation.sendSelectedCandidatesEmailsQuiz(savedQuizAttempt.getCondidacy().getOpportunity().getId_Opportunity()));
        scheduelRepository.save(schedule);
        quizAttempt.setScheduleSecondSelection(schedule);
        scheduelRepository.save(savedQuizAttempt.getScheduleSecondSelection());
        quizRepository.save(quiz);
        savedQuizAttempt.setCaptcha(generatedCaptcha);
        quizAttemptRepository.save(savedQuizAttempt);

        return ResponseEntity.ok(savedQuizAttempt);
    }


}
