package loader.exception;

public class EmailWasTakenException extends Exception {
    public EmailWasTakenException(String email){
        super("Email" + email + " has been already taken");
    }
}