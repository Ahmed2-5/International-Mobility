package tn.esprit.spring.services.Opportunity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Opportunity;
import tn.esprit.spring.entities.QuizAttempt;
import tn.esprit.spring.entities.Speciality;
import tn.esprit.spring.interfaces.StatInterface;
import tn.esprit.spring.repositories.OpportunityRepository;
import tn.esprit.spring.repositories.QuizAttemptRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatImplementation implements StatInterface {
    @Autowired
    OpportunityRepository opportunityRepository;
    @Override
    public Map<String, Double> getOpportunitiesPercentageBySpeciality() {


        List<String> specialities = opportunityRepository.findDistinctSpecialite();
        Map<String, Double> specialityPercentages = new HashMap<>();

        for (String speciality : specialities) {
            List<Opportunity> opportunities = opportunityRepository.findBySpecialite(Speciality.valueOf(speciality));
            int opportunitiesCount = opportunities.size();
            int totalOpportunities = opportunityRepository.findAll().size();
            double percentage = (opportunitiesCount / (double) totalOpportunities) * 100;
            specialityPercentages.put(speciality, percentage);
        }

        return specialityPercentages;
    }
@Autowired
    QuizAttemptRepository quizAttemptRepository;
    @Override
    public Map<String, Double> getAverageQuizScoreByOpportunityType() {
        List<Opportunity> opportunities = opportunityRepository.findAll();
        Map<String, Double> result = new HashMap<>();
        for (Opportunity opportunity : opportunities) {
            List<QuizAttempt> quizAttempts = quizAttemptRepository.findByCondidacy_Opportunity(opportunity);
            if (!quizAttempts.isEmpty()) {
                double avgScore = quizAttempts.stream().mapToDouble(QuizAttempt::getScore).average().orElse(0.0);
                result.put(opportunity.getType().toString(), avgScore);
            }
        }
        return result;
    }

}

