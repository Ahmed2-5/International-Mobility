package tn.esprit.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Opportunity;
import tn.esprit.spring.entities.RatingDTO;
import tn.esprit.spring.repositories.OpportunityRepository;

import java.util.Optional;

@RestController
@RequestMapping("/Rating")
public class RatingController {
    @Autowired
    OpportunityRepository opportunityRepository;
    @PostMapping("/opportunities/rating")
    public ResponseEntity<String> addRating(@RequestBody RatingDTO ratingDTO) {
        Optional<Opportunity> opportunityOptional = opportunityRepository.findById(Math.toIntExact(ratingDTO.getOpportunityId()));
        if (opportunityOptional.isPresent()) {
            Opportunity opportunity = opportunityOptional.get();
            int currentNumberOfRatings = opportunity.getNumberOfRatings();
            double currentAverageRating = opportunity.getAverageRating();
            double newAverageRating = (currentAverageRating * currentNumberOfRatings + ratingDTO.getRating()) / (currentNumberOfRatings + 1);
            opportunity.setNumberOfRatings(currentNumberOfRatings + 1);
            opportunity.setAverageRating(newAverageRating);
            opportunityRepository.save(opportunity);
            return ResponseEntity.ok("Rating added successfully");
        } else {
            return ResponseEntity.badRequest().body("Opportunity with id " + ratingDTO.getOpportunityId() + " not found");
        }

    }
    @GetMapping("/opportunities/{id}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Integer id) {
        Optional<Opportunity> opportunityOptional = opportunityRepository.findById(id);
        if (opportunityOptional.isPresent()) {
            Opportunity opportunity = opportunityOptional.get();
            return ResponseEntity.ok(opportunity.getAverageRating());
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }



}
