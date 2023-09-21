package tn.esprit.spring.services.Opportunity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.spring.entities.Condidacy;
import tn.esprit.spring.entities.Opportunity;
import tn.esprit.spring.entities.Schedule;
import tn.esprit.spring.entities.TypeScheduel;
import tn.esprit.spring.interfaces.Scheduel;
import tn.esprit.spring.repositories.OpportunityRepository;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;

public class ScheduelImplementation implements Scheduel {
    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    Pi_MobilityImplementation pi_mobilityImplementation;

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void ScheduelMailPreselection(Schedule scheduel, Integer opportunityId) throws MessagingException, IOException {

        Opportunity opportunity = opportunityRepository.findById(opportunityId).orElse(null);
        if (opportunity.getEndDate().isEqual(LocalDate.now())) {
            for (Condidacy condidacy : pi_mobilityImplementation.sendSelectedCandidatesEmailsTest(opportunityId)) {
                if (condidacy.isAttempted() == false) {
                    pi_mobilityImplementation.sendSelectedCandidatesEmailsTest(opportunityId);
                    scheduel.setStatus(1);
                    scheduel.setTypeScheduel(TypeScheduel.First_Selection);
                    scheduel.setEndDate(LocalDate.now());
                }

            }


        } else if (opportunity.getEndDate().isBefore(LocalDate.now()) && scheduel.getStatus() == 0) {
            scheduel.setStatus(2);
            scheduel.setEndDate(LocalDate.now());

        }


    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void ScheduelMailSecondSelection(Schedule scheduel, Integer opportunityId) throws MessagingException, IOException {
        Opportunity opportunity = opportunityRepository.findById(opportunityId).orElse(null);
        if (opportunity.getQuizzesQuiz().getEndDate().isEqual(LocalDate.now())) {
            for (Condidacy condidacy : pi_mobilityImplementation.sendSelectedCandidatesEmailsQuiz(opportunityId)) {
                pi_mobilityImplementation.sendSelectedCandidatesEmailsQuiz(opportunityId);
                scheduel.setStatus(1);
                scheduel.setTypeScheduel(TypeScheduel.Second_Selection);
                scheduel.setEndDate(LocalDate.now());

            }
        } else if (opportunity.getQuizzesQuiz().getEndDate().isBefore(LocalDate.now()) && scheduel.getStatus() == 0) {
            scheduel.setStatus(2);
            scheduel.setEndDate(LocalDate.now());

        }


    }
}
