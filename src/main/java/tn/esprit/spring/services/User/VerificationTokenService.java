package tn.esprit.spring.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.UserVerificationToken;
import tn.esprit.spring.repositories.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public UserVerificationToken createVerificationToken(User user) {
        String token = generateVerificationToken();
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);

        UserVerificationToken verificationToken = new UserVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(expiryDate);
        verificationTokenRepository.save(verificationToken);

        return verificationToken;
    }

    public UserVerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void saveVerificationToken(UserVerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    // méthode pour valider si le jeton est encore valide
    public boolean isValidVerificationToken(UserVerificationToken verificationToken) {
        return verificationToken != null && !isExpired();
    }
    public boolean isExpired() {
        UserVerificationToken verificationToken = new UserVerificationToken();
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(verificationToken.getExpiryDate());
    }
    // méthode pour générer un jeton aléatoire
    public String generateVerificationToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 30;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
