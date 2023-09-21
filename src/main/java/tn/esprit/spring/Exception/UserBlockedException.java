package tn.esprit.spring.Exception;

public class UserBlockedException extends Exception {
    public UserBlockedException() {
        super("User is blocked. Please try again later.");
    }
}
