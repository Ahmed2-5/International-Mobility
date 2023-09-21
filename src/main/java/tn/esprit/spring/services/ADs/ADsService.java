package tn.esprit.spring.services.ADs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Opportunity;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.UserADS;
import tn.esprit.spring.repositories.ADsRepository;
import tn.esprit.spring.repositories.OpportunityRepository;

import java.util.List;

@Service
public class ADsService {
    @Autowired
    private ADsRepository userADSRepository;


    @Autowired
    private OpportunityRepository opportunityRepository;


    public List<UserADS> getAllUserADS() {
        return userADSRepository.findAll();
    }

    public UserADS getUserADSById(Long id_ADS) {
        return userADSRepository.findById(id_ADS).orElse(null);
    }
    public List<UserADS> FindADsByStatus() {
        return userADSRepository.findByStatus();
    }

    public UserADS addUserADS(UserADS userADS) {

        userADS.setStatus(0);
        return userADSRepository.save(userADS);
    }
    public UserADS createAndAssignToOpportunity(UserADS userADS, Integer opportunityId) {
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found with id: " + opportunityId));

        userADS.setOpportunity(opportunity);
        UserADS savedUserADS = userADSRepository.save(userADS);
        opportunity.setUserADS(savedUserADS);
        opportunityRepository.save(opportunity);

        return savedUserADS;
    }
    public UserADS ValidateAD(String title) {
        UserADS ads = userADSRepository.findByAddstitle(title);
        if (ads != null) {
            ads.setStatus(1);

            userADSRepository.save(ads);
        }
        return ads;
    }
    public UserADS updateUserADS(UserADS userADS) {
        return userADSRepository.save(userADS);
    }

    public void deleteUserADS(Long id_ADS) {
        userADSRepository.deleteById(id_ADS);
    }


}
