package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Condidacy;
@Repository
public interface CondidacyRepository extends JpaRepository  <Condidacy ,Integer> {
}
