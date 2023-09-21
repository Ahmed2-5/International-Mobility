package tn.esprit.spring.interfaces;

import java.util.Map;

public interface StatInterface {
    public Map<String, Double> getOpportunitiesPercentageBySpeciality();

    Map<String, Double> getAverageQuizScoreByOpportunityType();
}
