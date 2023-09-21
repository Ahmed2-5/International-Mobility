package tn.esprit.spring.interfaces;


import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Appointement;
import tn.esprit.spring.entities.WaitingList;

import java.time.LocalDate;
import java.util.List;
@Service
public interface IAppointementService {




    public List<Appointement> retrieveAllRdv();
    public Appointement updateRdv(Appointement ap) ;
    public Appointement addRdv(Appointement ap) ;

    public Appointement retrieveRdv(Integer idAppointement) ;

    public void removeRdv(Integer idAppointement);
    public Appointement updateById(Integer idAppointement, Appointement appointement);
    public List<Appointement> findByDateRangeSortedByDateDemande(LocalDate startDate, LocalDate endDate);
    public boolean isAppointmentAvailable(LocalDate date);

  //  void cancelAppointment(Integer idAppointement);
  //public void scheduleAppointmentFromWaitingList(LocalDate dateDemande) ;
  public WaitingList getWaitingListById(Integer idWaiting) ;

    Appointement assignRoleToAppointment(Integer appointmentId, String roleName);

    void sendReminderEmail(Appointement appointment);
    public void blockDates(List<LocalDate> datesToBlock);
}
