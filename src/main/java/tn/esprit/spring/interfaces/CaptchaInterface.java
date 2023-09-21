package tn.esprit.spring.interfaces;

import tn.esprit.spring.entities.Captcha;

import java.util.List;

public interface CaptchaInterface {
    public List<Captcha> generateCaptchas(int numCaptchas, int captchaLength);
}
