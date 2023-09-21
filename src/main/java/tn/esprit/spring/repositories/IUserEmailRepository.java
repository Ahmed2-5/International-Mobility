package tn.esprit.spring.repositories;

import tn.esprit.spring.entities.UserMail;

public interface IUserEmailRepository {
    public void sendCodeByMail(UserMail mail);
}