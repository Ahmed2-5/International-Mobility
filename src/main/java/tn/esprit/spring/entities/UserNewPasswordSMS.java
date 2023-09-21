package tn.esprit.spring.entities;

import lombok.Data;

@Data
public class UserNewPasswordSMS {
    private String phone;
    private String code;
    private String password;
}