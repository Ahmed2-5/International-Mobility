package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.UserVerificationToken;


@Repository
public interface VerificationTokenRepository extends JpaRepository<UserVerificationToken, Long> {
    UserVerificationToken findByToken(String Token);
}
