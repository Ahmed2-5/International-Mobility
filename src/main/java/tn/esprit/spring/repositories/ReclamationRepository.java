package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.spring.entities.Reclamation;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {

   @Query("SELECT r FROM Reclamation r WHERE r.statu = :status")
    List<Reclamation> findByStatus(@Param("status") String status);

    @Query("SELECT u.Gender, COUNT(r) FROM Reclamation r JOIN r.user u GROUP BY u.Gender")
    List<Object[]> countByUserGender();


    @Query("SELECT r.Objet, COUNT(r) FROM Reclamation r GROUP BY r.Objet")
    List<Object[]> countByObjet();

}
