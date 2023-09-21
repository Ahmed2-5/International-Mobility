package tn.esprit.spring.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Historique;

@Repository
public interface IHistoriqueRepository extends JpaRepository<Historique, Integer> {
}
