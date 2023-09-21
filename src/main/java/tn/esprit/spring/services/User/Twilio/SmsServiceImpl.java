package tn.esprit.spring.services.User.Twilio;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.configuration.UserSMS.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class SmsServiceImpl {

    @Autowired
    private TwilioConfig twilioConfig;

    //SMS
    public void SendSMS(String toNumber,String code){
        // Find your Account Sid and Token at twilio.com/user/account
        Twilio.init(twilioConfig.getAccount_sid(), twilioConfig.getAuth_token());

        Message message = Message.creator(new PhoneNumber("+216" + toNumber),
                new PhoneNumber(twilioConfig.getSender_number()),
                code).create();

        System.out.println(message.getSid());
    }


}