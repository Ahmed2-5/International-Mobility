package tn.esprit.spring.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.spring.entities.Appointement;
import tn.esprit.spring.services.Appintment.AppointementServiceImpl;


import java.time.LocalDate;
import java.util.List;

@Configuration
public class AppointmentCanceler {
    @Autowired
    private AppointementServiceImpl appointementService;

    @Scheduled(fixedRate = 86400000) //exécuter toutes les 24 heures
    public void cancelExpiredAppointments() {
        // Calculer la date limite : maintenant moins un mois
        LocalDate dateRdv = LocalDate.now().minusMonths(1);

        // Trouver tous les rendez-vous actifs qui ont dépassé la date limite
        List<Appointement> expiredAppointments = appointementService.findExpiredAppointments(dateRdv);

        // Annuler tous les rendez-vous qui ont dépassé la date limite
        for (Appointement appointment : expiredAppointments) {
            appointementService.removeRdv(appointment.getIdAppointement());
        }
    }
}
