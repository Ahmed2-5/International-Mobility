package tn.esprit.spring.configuration.UserSMS;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twilio")
@Data
public class TwilioConfig {

    private String account_sid;
    private String auth_token;
    private String sender_number;

}

