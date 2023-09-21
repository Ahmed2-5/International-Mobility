package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.UserADS;

import java.util.List;

@Repository
public interface ADsRepository extends JpaRepository<UserADS,Long> {
 UserADS findByAddstitle(String title);
    @Query("SELECT u FROM UserADS u WHERE u.status = 1")
    List<UserADS> findByStatus();
}
