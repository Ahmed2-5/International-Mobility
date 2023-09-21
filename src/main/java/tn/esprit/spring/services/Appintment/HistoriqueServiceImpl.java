package tn.esprit.spring.services.Appintment;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Appointement;
import tn.esprit.spring.entities.Historique;
import tn.esprit.spring.interfaces.IHistoriqueService;
import tn.esprit.spring.repositories.AppointementRepository;
import tn.esprit.spring.repositories.IHistoriqueRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HistoriqueServiceImpl implements IHistoriqueService {
    @Autowired
    IHistoriqueRepository historiquerepository;
    @Autowired
    AppointementRepository appointementrepository;

    @Override
    public List<Historique> retrieveAllHist() {
        return null;
    }



    @Override
    public Historique addHistorique(Integer idAppointement) {
        Appointement appointment = appointementrepository.findById(idAppointement).orElse(null);
        Historique hq = new Historique();
        hq.setEmail(appointment.getEmail());
        hq.setPhoneNumber(appointment.getPhoneNumber());
        hq.setDateRdv(appointment.getDateRdv());

        return historiquerepository.save(hq);
    }
    @Override
    public Historique updateHistorique(Integer idAppointement, Integer DurationAppointment, String NamePartner) {
        Historique historique = historiquerepository.findById(idAppointement).orElse(null);

        if (historique != null) {
            historique.setDurationAppointment(DurationAppointment);
            historique.setNamePartner(NamePartner);
            historique = historiquerepository.save(historique);
        }

        return historique;
    }

/*    public String generateAppointmentReport(int idAppointement , String NamePartner) {
        List<Historique> historiques = historiquerepository.findAll();


        if (NamePartner != null && !NamePartner.isEmpty()) {
            historiques = historiques.stream()
                    .filter(a -> a.getNamePartner().equalsIgnoreCase(NamePartner))
                    .collect(Collectors.toList());
        }

        // Build the report string
        StringBuilder reportBuilder = new StringBuilder();
        for (Historique appointment : historiques) {
            reportBuilder.append("Appointment ID: ").append(appointment.getIdAppointement()).append("\n");
            reportBuilder.append("Email: ").append(appointment.getEmail()).append("\n");
            reportBuilder.append("Phone Number: ").append(appointment.getPhoneNumber()).append("\n");
            reportBuilder.append("Date: ").append(appointment.getDateRdv()).append("\n");
            reportBuilder.append("Duration: ").append(appointment.getDurationAppointment()).append("\n");
            reportBuilder.append("Partner Name: ").append(appointment.getNamePartner()).append("\n");
            reportBuilder.append("Report: ").append(appointment.getReport()).append("\n");
            reportBuilder.append("\n");
        }

        return reportBuilder.toString();
    }*/
public String generateAppointmentReport(int idAppointement, String NamePartner) {
    List<Historique> historiques = historiquerepository.findAll();

    if (NamePartner != null && !NamePartner.isEmpty()) {
        historiques = historiques.stream()
                .filter(a -> a.getNamePartner().equalsIgnoreCase(NamePartner))
                .collect(Collectors.toList());
    }

    // Build the report HTML string
    StringBuilder reportBuilder = new StringBuilder();
    reportBuilder.append("<html><head><title>Appointment Report</title></head><body>");
    reportBuilder.append("<h1>Appointment Report</h1>");
    reportBuilder.append("<table><tr><th>ID</th><th>Email</th><th>Phone Number</th><th>Date</th><th>Duration</th><th>Partner Name</th><th>Report</th></tr>");
    for (Historique appointment : historiques) {
        reportBuilder.append("<tr>");
        reportBuilder.append("<td>").append(appointment.getIdAppointement()).append("</td>");
        reportBuilder.append("<td>").append(appointment.getEmail()).append("</td>");
        reportBuilder.append("<td>").append(appointment.getPhoneNumber()).append("</td>");
        reportBuilder.append("<td>").append(appointment.getDateRdv()).append("</td>");
        reportBuilder.append("<td>").append(appointment.getDurationAppointment()).append("</td>");
        reportBuilder.append("<td>").append(appointment.getNamePartner()).append("</td>");
        reportBuilder.append("<td>").append(appointment.getReport()).append("</td>");
        reportBuilder.append("</tr>");
    }
    reportBuilder.append("</table></body></html>");

    return reportBuilder.toString();
}

    public double getAverageDuration() {
        List<Historique> historiques = historiquerepository.findAll();
        double sumDuration = historiques.stream().mapToInt(Historique::getDurationAppointment).sum();
        return sumDuration / historiques.size();
    }

    @Override
    public List<Historique> getHistoriques() {
        return null;
    }

    @Override
    public List<Historique> getHistoriquesByPartner(String partnerName) {
        return null;
    }


    public String getPartnerWithMostAppointments() {
        Map<String, Long> counts = countByPartner();
        return counts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private Map<String, Long> countByPartner() {
        List<Historique> historiques = historiquerepository.findAll();
        return historiques.stream()
                .collect(Collectors.groupingBy(Historique::getNamePartner, Collectors.counting()));
    }



}

