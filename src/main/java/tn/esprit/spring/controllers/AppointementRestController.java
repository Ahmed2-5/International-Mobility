package tn.esprit.spring.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Appointement;
import tn.esprit.spring.entities.Historique;
import tn.esprit.spring.entities.WaitingList;
import tn.esprit.spring.interfaces.IAppointementService;
import tn.esprit.spring.interfaces.IHistoriqueService;
import tn.esprit.spring.repositories.AppointementRepository;
import tn.esprit.spring.repositories.IHistoriqueRepository;

import javax.imageio.ImageIO;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
//@AllArgsConstructor
@RequestMapping("/rendezvous")
public class AppointementRestController {
    @Autowired
    // @Qualifier("AppointementServiceImpl")
    IHistoriqueService historiqueService;


    @Autowired
    // @Qualifier("AppointementServiceImpl")
    IAppointementService appointementService;

    @Autowired
    // @Qualifier("AppointementServiceImpl")
    AppointementRepository appointementRepository;
    @Autowired
    private JavaMailSender mailSenderObj;
    @Autowired
    IHistoriqueRepository historiquerepository;

    @GetMapping("/retrieve-all-rendezvous")
    public List<Appointement> getAppointement() {
        List<Appointement> appointementList = appointementService.retrieveAllRdv();
        return appointementList;

    }

    @GetMapping("/retrieve-rendezvous/{rendezvous-id}")
    public Appointement retrieveAppointement(@PathVariable("rendezvous-id") Integer idAppointement) {
        return appointementService.retrieveRdv(idAppointement);
    }
    @PostMapping("/addappointment")
    public ResponseEntity<?> addAppointment(@RequestBody Appointement appointment) {
        try {
            Appointement savedAppointment = appointementService.addRdv(appointment);
            return ResponseEntity.ok(savedAppointment);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add appointment.");
        }
    }

   /* @PostMapping("/add-rendezvous")
    public Appointement addRdv(@RequestBody Appointement a) {
        Appointement savedAppointment = appointementService.addRdv(a);

        return savedAppointment;
    }
*/
    @DeleteMapping("/remove-rendezvous/{appointement-id}")
    public void removeRdv(@PathVariable("appointement-id") Integer idAppointement) {

        appointementService.removeRdv(idAppointement);
    }

    @PutMapping("/update-rdv")
    public Appointement updateRdv(@RequestBody Appointement a) {
        Appointement appointement = appointementService.updateRdv(a);
        return appointement;
    }

    @PutMapping("/{id}")
    public Appointement updateAppointementById(@PathVariable("id") Integer idAppointement,
                                               @RequestBody Appointement appointement) {
        Appointement updatedAppointement = appointementService.updateById(idAppointement, appointement);

        return appointement;
    }

    @GetMapping("/range-all-rendezvous")
    public List<Appointement> getMyAppointementByDateRangeSortedByDateDemande(
            @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return appointementService.findByDateRangeSortedByDateDemande(startDate, endDate);
    }
    @GetMapping("/checkAvailability")
    public ResponseEntity<?> checkAvailability(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean isAvailable = appointementService.isAppointmentAvailable(date);

        if (isAvailable) {
            return ResponseEntity.ok("Appointment is available on " + date);
        } else {
            return ResponseEntity.badRequest().body("Appointment is not available on " + date);
        }
    }

    @PostMapping("/appointments/{id}/convert")
    public ResponseEntity<Historique> convertAppointmentToHistorique(@PathVariable("id") Integer id) {
        Historique historique = historiqueService.addHistorique(id);
        if (historique == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(historique);
        }

        }
    @PutMapping("/historique/{idAppointement}")
    public ResponseEntity<Historique> updateHistorique(@PathVariable Integer idAppointement,
                                                       @RequestParam Integer durationAppointment,
                                                       @RequestParam String namePartner) {

        Historique updatedHistorique = historiqueService.updateHistorique(idAppointement, durationAppointment, namePartner);

        if (updatedHistorique == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedHistorique);
    }

/*    @GetMapping("/report")
    public String generateAppointmentReport(@RequestParam(required = false) Integer id, @RequestParam(required = false) String NamePartner) {
        String report = historiqueService.generateAppointmentReport(id, NamePartner);
        return report;
    }*/

    @GetMapping("/appointment/report")
    public ResponseEntity<String> getAppointmentReport(@RequestParam(required = false) Integer idAppointment,
                                                       @RequestParam(required = false) String namePartner) {
        String reportHtml = generateAppointmentReport(idAppointment, namePartner);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(reportHtml, headers, HttpStatus.OK);
    }

    private String generateAppointmentReport(Integer idAppointment, String namePartner) {
        List<Historique> historiques = historiquerepository.findAll();

        if (idAppointment != null) {
            historiques = historiques.stream()
                    .filter(a -> a.getIdAppointement() == idAppointment)
                    .collect(Collectors.toList());
        }

        if (namePartner != null && !namePartner.isEmpty()) {
            historiques = historiques.stream()
                    .filter(a -> a.getNamePartner().equalsIgnoreCase(namePartner))
                    .collect(Collectors.toList());
        }

        // Build the report HTML string
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("<html><head><title>Appointment Report</title>");
        reportBuilder.append("<style>table {border-collapse: collapse; width: 100%;} th, td {text-align: left; padding: 8px;} th {background-color: #4CAF50; color: white;}</style>");
        reportBuilder.append("</head><body>");
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
            reportBuilder.append("<td>").append("Appointment report: ").append(appointment.getReport()).append("</td>");
            reportBuilder.append("</tr>");
        }
        reportBuilder.append("</table></body></html>");

        return reportBuilder.toString();
    }


    /* @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<Object> cancelAppointment(@PathVariable Integer idAppointement) {
        appointementService.cancelAppointment(idAppointement);
        return ResponseEntity.ok().build();
    }*/
   @GetMapping("/retrieve-waiting-list/{waiting-list-id}")
   public WaitingList retrieveWaitingList(@PathVariable("waiting-list-id") Integer idWaiting) {
       return appointementService.getWaitingListById(idWaiting);
   }
    @GetMapping(value = "/displayQRCode/{idWaiting}")
    public ResponseEntity<byte[]> displayQRCode(@PathVariable("idWaiting") Integer idWaiting)
            throws Exception {
        WaitingList waitingList = appointementService.getWaitingListById(idWaiting);
        BufferedImage qrCodeImage = QRCodeGenerator.generateQRCodeImageForWaitingList(waitingList);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageBytes, headers, HttpStatus.OK);
    }


    @PostMapping("/{id}/assign-role")
    public ResponseEntity<Appointement> assignRoleToAppointment(
            @PathVariable("id") Integer appointmentId,
            @RequestParam("role") String roleName) {
        Appointement appointment = appointementService.assignRoleToAppointment(appointmentId, roleName);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping(value = "/sendMail/{idAppointement}")
    public ResponseEntity<Appointement> employeeMail(@PathVariable("idAppointement") Integer idAppointement) {

        Appointement appointement = appointementRepository.findById(idAppointement).get();

        sendmail(appointement);

        return new ResponseEntity<Appointement>(HttpStatus.OK);

    }

    private void sendmail(Appointement appointement) {

        final String emailToRecipient = appointement.getEmail();
        final String emailSubject = "Succesfully Registration";
        final String emailMessage1 = "<html> <body> <p>Dear Sir/Madam,</p><p>You have succesfully Registered with our Services"
                + "<br><br>"
                + "<table border='1' width='300px' style='text-align:center;font-size:20px;'><tr> <td colspan='2'>"
                + "</td></tr><tr><td>Name</td><td>" + appointement.getFirstname() + "</td></tr><tr><td>Address</td><td>"
                + appointement.getDateRdv() + "</td></tr><tr><td>Email</td><td>" + appointement.getEmail()
                + "</td></tr></table> </body></html>";

        mailSenderObj.send(new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                mimeMsgHelperObj.setTo(emailToRecipient);
                mimeMsgHelperObj.setText(emailMessage1, true);

                mimeMsgHelperObj.setSubject(emailSubject);

            }
        });

    }
    @PostMapping("/appointments/{idAppointement}/send-reminder")
    public ResponseEntity<Void> sendReminderEmail(@PathVariable Integer idAppointement) {
        Appointement appointment = appointementRepository.findById(idAppointement)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + idAppointement));
        appointementService.sendReminderEmail(appointment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats/average-duration/chart")
    public double getPieChart(Model model) {
        double averageDuration = historiqueService.getAverageDuration();
        model.addAttribute("averageDuration", averageDuration);
        return averageDuration;
    }
    @GetMapping("/stats/average-duration-stat")
    public String showAverageDurationStat(Model model) {
        double averageDuration = historiqueService.getAverageDuration();
        model.addAttribute("averageDuration", averageDuration);
        return "average-duration-stat";
    }
    @GetMapping("/stats/average")
    public String showDashboard(Model model) {
        // retrieve claim statistics...
        double averageDuration = historiqueService.getAverageDuration();

        // add average duration to the model
        model.addAttribute("averageDuration", averageDuration);

        // return the name of the view to render
        return "dashboard";
    }
    @GetMapping("/average-duration")
    public String showAverageDurationPage() {
        return "average-duration";
    }
    @GetMapping("/historique")
    public String showHistorique(Model model) {
        List<Historique> historiques = historiqueService.getHistoriques();
        double averageDuration = historiqueService.getAverageDuration();
        model.addAttribute("historiques", historiques);
        model.addAttribute("averageDuration", averageDuration);
        return "historique";
    }
    @PostMapping("/block-dates")
    public ResponseEntity<String> blockDates(@RequestBody List<LocalDate> datesToBlock) {
        appointementService.blockDates(datesToBlock);
        return ResponseEntity.ok("Dates blocked successfully.");
    }
    @GetMapping("/partner/{partnerName}")
    public List<Historique> getHistoriquesByPartner(@PathVariable String partnerName) {
        return historiqueService.getHistoriquesByPartner(partnerName);
    }



    @GetMapping("/partnerWithMostAppointments")
    public String getPartnerWithMostAppointments() {
        return historiqueService.getPartnerWithMostAppointments();
    }

}






