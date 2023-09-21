package tn.esprit.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.ADsRatingDTO;
import tn.esprit.spring.entities.Opportunity;
import tn.esprit.spring.entities.RatingDTO;
import tn.esprit.spring.entities.UserADS;
import tn.esprit.spring.repositories.ADsRepository;
import tn.esprit.spring.repositories.OpportunityRepository;

import java.util.Optional;
@RestController
@RequestMapping("/Rating")
public class ADsRatingController {
    @Autowired
    ADsRepository aDsRepository;
    @PostMapping("/ADS/rating")
    public ResponseEntity<String> addRating(@RequestBody ADsRatingDTO ratingDTO) {
        Optional<UserADS> ADSOptional = aDsRepository.findById((long) Math.toIntExact(ratingDTO.getAds_ID()));
        if (ADSOptional.isPresent()) {
            UserADS ADS = ADSOptional.get();
            int currentNumberOfRatings = ADS.getNumberOfRatings();
            double currentAverageRating = ADS.getAverageRating();
            double newAverageRating = (currentAverageRating * currentNumberOfRatings + ratingDTO.getAds_ID()) / (currentNumberOfRatings + 1);
            ADS.setNumberOfRatings(currentNumberOfRatings + 1);
            ADS.setAverageRating(newAverageRating);
            aDsRepository.save(ADS);
            return ResponseEntity.ok("Rating added successfully");
        } else {
            return ResponseEntity.badRequest().body("ADs with id " + ratingDTO.getAds_ID() + " not found");
        }

    }
    @GetMapping("/ADS/{id}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long id) {
        Optional<UserADS> adsOptional = aDsRepository.findById(id);
        if (adsOptional.isPresent()) {
            UserADS ads = adsOptional.get();
            return ResponseEntity.ok(ads.getAverageRating());
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
