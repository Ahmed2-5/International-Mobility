package tn.esprit.spring.interfaces;

import tn.esprit.spring.entities.Schedule;

import javax.mail.MessagingException;
import java.io.IOException;

public interface Scheduel {
    void ScheduelMailPreselection(Schedule scheduel, Integer opportunityId)throws MessagingException, IOException;;
    void ScheduelMailSecondSelection(Schedule scheduel,Integer opportunityId)throws MessagingException, IOException ;;

}
