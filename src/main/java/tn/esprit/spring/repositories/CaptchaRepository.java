package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Captcha;

import java.util.List;

@Repository
public interface CaptchaRepository extends JpaRepository <Captcha,Integer> {
    List<Captcha> findByIsUsed(boolean isUsed);
    Captcha findFirstByIsUsed(boolean isUsed);


}
