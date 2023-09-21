package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.entities.Foyer;

public interface FoyerRepository extends JpaRepository<Foyer, Long> {
}
