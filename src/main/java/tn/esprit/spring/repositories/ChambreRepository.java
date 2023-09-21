package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.entities.Chambre;

public interface ChambreRepository extends JpaRepository<Chambre,Long > {
}
