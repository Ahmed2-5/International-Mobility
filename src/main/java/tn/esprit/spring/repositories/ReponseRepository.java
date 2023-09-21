package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.spring.entities.Reponse;

import java.util.List;

public interface ReponseRepository extends JpaRepository<Reponse,Integer> {

    @Query("SELECT r FROM Reponse r JOIN r.ratings ra GROUP BY r.IdReponse ORDER BY AVG(ra.value) DESC")
    List<Reponse> findReponseWithHighestRating();
}
