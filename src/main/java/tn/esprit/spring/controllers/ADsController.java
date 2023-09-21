package tn.esprit.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.UserADS;
import tn.esprit.spring.repositories.ADsRepository;
import tn.esprit.spring.repositories.UserRepository;
import tn.esprit.spring.services.ADs.ADsService;

import java.util.List;

@RestController
public class ADsController {

    @Autowired
    private ADsService userADSService;

    @GetMapping("/")
    public List<UserADS> getAllUserADS() {
        return userADSService.getAllUserADS();
    }

    @GetMapping("/{id_ADS}")
    public UserADS getUserADSById(@PathVariable Long id_ADS) {
        return userADSService.getUserADSById(id_ADS);
    }
    @PostMapping("/createAndAssignToOpportunity")
    public UserADS createAndAssignToOpportunity(@RequestBody UserADS userADS, @RequestParam Integer opportunityId) {
        userADS.setStatus(0);
        return userADSService.createAndAssignToOpportunity(userADS, opportunityId);
    }
    @PutMapping("/ApproveAdd/{title}")
   // @PreAuthorize("hasRole('Admin')")
    public void ApproveAdd(@PathVariable String title) {

        userADSService.ValidateAD(title);
    }

    @PostMapping("/CreateADs")
    public UserADS addUserADS(@RequestBody UserADS userADS) {
        return userADSService.addUserADS(userADS);
    }

    @PutMapping("/updateADs")
    public UserADS updateUserADS(@RequestBody UserADS userADS) {
        return userADSService.updateUserADS(userADS);
    }

    @DeleteMapping("/DeleteADs/{id_ADS}")
    public void deleteUserADS(@PathVariable Long id_ADS) {
        userADSService.deleteUserADS(id_ADS);
    }

}
