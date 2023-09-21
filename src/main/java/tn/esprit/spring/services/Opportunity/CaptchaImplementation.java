package tn.esprit.spring.services.Opportunity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Captcha;
import tn.esprit.spring.interfaces.CaptchaInterface;
import tn.esprit.spring.repositories.CaptchaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@Slf4j
@Service
public class CaptchaImplementation implements CaptchaInterface {
    @Autowired
    CaptchaRepository captchaRepository;
    @Override
    public List<Captcha> generateCaptchas(int numCaptchas, int captchaLength) {
        List<Captcha> captchas = new ArrayList<>();
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        for (int i = 0; i < numCaptchas; i++) {
            StringBuilder captchaValue = new StringBuilder();
            for (int j = 0; j < captchaLength; j++) {
                int index = random.nextInt(allowedChars.length());
                captchaValue.append(allowedChars.charAt(index));
            }

            Captcha captcha = new Captcha();
            captcha.setCaptchaValue(captchaValue.toString());
            captcha.setUsed(false);
            captchas.add(captcha);
        }

        captchaRepository.saveAll(captchas);
        return captchas;
    }

    public String getRandomCaptcha() {
        Captcha randomCaptcha = captchaRepository.findFirstByIsUsed(false);
        if (randomCaptcha == null) {
            // no available captchas in database, generate new set of captchas
            generateCaptchas(10, 6); // generate 10 captchas with length of 6 characters each
            randomCaptcha = captchaRepository.findFirstByIsUsed(false);
        }

        randomCaptcha.setUsed(true);
        captchaRepository.save(randomCaptcha);

        // generate new captcha
        Captcha newCaptcha = new Captcha();
        newCaptcha.setCaptchaValue(generateRandomCaptcha(6));
        newCaptcha.setUsed(false);
        captchaRepository.save(newCaptcha);

        return randomCaptcha.getCaptchaValue();
    }
    private String generateRandomCaptcha(int captchaLength) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder captchaValue = new StringBuilder();
        for (int j = 0; j < captchaLength; j++) {
            int index = random.nextInt(allowedChars.length());
            captchaValue.append(allowedChars.charAt(index));
        }
        return captchaValue.toString();
    }
}
