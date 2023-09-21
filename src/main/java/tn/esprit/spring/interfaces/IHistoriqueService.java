package tn.esprit.spring.interfaces;

import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Historique;

import java.util.List;

@Service
public interface IHistoriqueService {

    public List<Historique> retrieveAllHist();

    public Historique addHistorique(Integer idAppointement) ;

    Historique updateHistorique(Integer idAppointement, Integer DurationAppointment, String NamePartner);
    public String generateAppointmentReport(int idAppointement , String NamePartner);

    double getAverageDuration();

    List<Historique> getHistoriques();

    List<Historique> getHistoriquesByPartner(String partnerName);

    String getPartnerWithMostAppointments();
}
