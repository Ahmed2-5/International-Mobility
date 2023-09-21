package tn.esprit.spring.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.WaitingList;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Integer> {
   WaitingList findByIdWaiting(Integer idWaiting);

   // List<WaitingList> findByDateRequested(LocalDate dateDemande);
   @Query("SELECT w FROM WaitingList w JOIN FETCH w.appointement a WHERE a.idAppointement = :appointmentId")
   WaitingList findByAppointmentIdWithAppointment(@Param("appointmentId") Integer appointmentId);
}


