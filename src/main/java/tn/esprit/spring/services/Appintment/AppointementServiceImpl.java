package tn.esprit.spring.services.Appintment;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Appointement;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.WaitingList;
import tn.esprit.spring.interfaces.IAppointementService;
import tn.esprit.spring.repositories.AppointementRepository;
import tn.esprit.spring.repositories.RoleRepository;
import tn.esprit.spring.repositories.WaitingListRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AppointementServiceImpl implements IAppointementService {
    @Autowired
    AppointementRepository appointementrepository;
    @Autowired
    WaitingListRepository waitingListRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public List<Appointement> retrieveAllRdv() {
        return null;
    }
    public Appointement updateRdv(Appointement ap) {

        return appointementrepository.save(ap);
    }

    /*public Appointement addRdv(Appointement ap) {

        Appointement savedAppointment =  appointementrepository.save(ap);
       // sendNotificationEmail(savedAppointment);
        return savedAppointment;

    }*/
    public Appointement addRdv(Appointement ap) {
        if (ap.getDateRdv() == null || ap.getEmail() == null || ap.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Missing required fields.");
        }
        // Check that the email address is valid
        if (!isValidEmailAddress(ap.getEmail())) {
            throw new IllegalArgumentException("Invalid email address.");
        }

        LocalDate date = ap.getDateRdv();
        if (!isAppointmentAvailable(date)) {
            WaitingList waitingList = new WaitingList();
            waitingList.setEmail(ap.getEmail());
            waitingList.setPhoneNumber(ap.getPhoneNumber());
            waitingList.setDateDemande(ap.getDateDemande());
            waitingList.setPaiment(0f);
            waitingList.setAppointement(ap);
            waitingListRepository.save(waitingList);
            throw new RuntimeException("Appointment is not available on " + date + ", added to waiting list.");
        }
        Appointement savedAppointment = appointementrepository.save(ap);
        // sendNotificationEmail(savedAppointment);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(ap.getEmail());
        message.setSubject("Appointment Accepted");
        message.setText("Your appointment on " + ap.getDateRdv() + " has been accepted.");
        javaMailSender.send(message);
        return savedAppointment;
    }
    private boolean isValidEmailAddress(String email) {
        if (email == null) {
            return false;
        }
        // Basic email format validation
        String emailRegex = "^[\\w-_.+]*[\\w-_.]@([\\w]+[.])+[\\w]{2,}$";
        return email.matches(emailRegex);
    }
    public Appointement retrieveRdv(Integer idAppointement) {

        return appointementrepository.findById(idAppointement).orElse(null);
    }

    public void removeRdv(Integer idAppointement) {
        appointementrepository.deleteById(idAppointement);
    }

    public Appointement updateById(Integer idAppointement, Appointement appointement) {
        Optional<Appointement> optionalAppointement = appointementrepository.findById(idAppointement);

        if (optionalAppointement.isPresent()) {
            Appointement existingAppointement = optionalAppointement.get();
            existingAppointement.setFirstname(appointement.getFirstname());
            existingAppointement.setLastname(appointement.getLastname());
            existingAppointement.setEmail(appointement.getEmail());
            existingAppointement.setPhoneNumber(appointement.getPhoneNumber());
            existingAppointement.setDateDemande(appointement.getDateDemande());
            existingAppointement.setDateRdv(appointement.getDateRdv());
            existingAppointement.setStatus(appointement.getStatus());
            return appointementrepository.save(existingAppointement);
        } return null ;

}

    public List<Appointement> findByDateRangeSortedByDateDemande(LocalDate startDate, LocalDate endDate) {
        return appointementrepository.findByDateDemandeBetweenOrderByDateDemande(startDate, endDate);
    }
    public boolean isAppointmentAvailable(LocalDate date) {
        int count = appointementrepository.countAppointmentsOnDate(date);
        return count == 0;
    }

   /* public void scheduleAppointmentFromWaitingList(LocalDate dateDemande) {
        List<WaitingList> waitingList = waitingListRepository.findByDateRequested(dateDemande);
        for (WaitingList waitingAppointment : waitingList) {
            if (isAppointmentAvailable(waitingAppointment.getAppointement().getDateDemande())) {
                Appointement appointment = waitingAppointment.getAppointement();
                appointementrepository.save(appointment); // Save the appointment to the appointment table
                waitingListRepository.delete(waitingAppointment); // Remove the appointment from the waiting list
            }
        }
    }*/

/*    public void cancelAppointment(Integer idAppointement) {
        Optional<Appointement> appointmentOptional = appointementrepository.findById(idAppointement);
        if (appointmentOptional.isPresent()) {
            Appointement appointement = appointmentOptional.get();
            appointement.setStatus(false);
            appointementrepository.save(appointement);
        }
    }*/
    public List<Appointement> findExpiredAppointments(LocalDate dateRdv) {
        return appointementrepository.findByStatusAndDateRdvBefore(true, dateRdv);
    }
    public WaitingList getWaitingListById(Integer idWaiting) {
        return waitingListRepository.findByIdWaiting(idWaiting);
    }
    @Override
    public Appointement assignRoleToAppointment(Integer appointmentId, String roleName) {
        Appointement appointment = appointementrepository.findById(appointmentId).orElse(null);
        Role role = roleRepository.findByRoleName(roleName);
        appointment.setRole(role);
        return appointementrepository.save(appointment);
    }
    public void sendReminderEmail(Appointement appointment) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        if (appointment.getDateRdv().equals(tomorrow) && appointment.getStatus()) {
            // send email using JavaMail API or another email library
            String recipient = appointment.getEmail();
            String subject = "Appointment Reminder";
            String message = "Dear " + appointment.getFirstname() + " " + appointment.getLastname() + ",\n\n"
                    + "This is a reminder that your appointment is scheduled for tomorrow, "
                    + appointment.getDateRdv().toString() + ".\n\n"
                    + "Please let us know if you need to reschedule or cancel the appointment.\n\n"
                    + "Thank you,\n"
                    + "Your Appointment Team";
            // send the email using your email library
        }
    }

    public void blockDates(List<LocalDate> datesToBlock) {
        LocalDate currentDate = LocalDate.now();
        List<Appointement> appointments = appointementrepository.findAll();
        for (LocalDate date : datesToBlock) {
            if (date.isBefore(currentDate)) {
                continue;
            }

            for (Appointement appointment : appointments) {
                if (appointment.getDateRdv().equals(date)) {
                    appointment.setStatus(false);
                    appointementrepository.save(appointment);
                }
            }
        }
    }


    }

