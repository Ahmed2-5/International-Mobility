package tn.esprit.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import tn.esprit.spring.entities.Appointement;
import tn.esprit.spring.repositories.AppointementRepository;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class AppointmentBlocker {
    @Autowired
    private AppointementRepository appointmentRepository;

    /**
     * Block one or more dates for all appointments.
     */
   // @Scheduled(cron = "0 0/1 * * * ?") // run every day at midnight
    public void blockDates() {
        LocalDate today = LocalDate.now();
        // block the next 3 days
        for (int i = 0; i < 3; i++) {
            LocalDate dateToBlock = today.plusDays(i);
            List<Appointement> appointments = appointmentRepository.findByDateRdv(dateToBlock);
            for (Appointement appointment : appointments) {
                appointment.setStatus(false); // set status to blocked
                appointmentRepository.save(appointment);
            }
        }
    }
}
