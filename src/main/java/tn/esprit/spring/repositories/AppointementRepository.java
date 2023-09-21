package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Appointement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointementRepository extends JpaRepository<Appointement, Integer> {
    Optional<Appointement> findByIdAppointement(Integer idAppointement);
    List<Appointement> findByDateDemandeBetweenOrderByDateDemande(LocalDate startDate, LocalDate endDate);
    @Query("SELECT COUNT(a) FROM Appointement a WHERE a.dateRdv = :date AND a.status = true")
    int countAppointmentsOnDate(@Param("date") LocalDate date);


    List<Appointement> findByStatusAndDateRdvBefore(boolean b, LocalDate dateRdv);

    List<Appointement> findByDateRdv(LocalDate tomorrow);
}




