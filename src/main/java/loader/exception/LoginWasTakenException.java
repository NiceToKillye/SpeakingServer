package loader.exception;

public class LoginWasTakenException extends Exception {
    public LoginWasTakenException(String login){
        super("Login " + login + " has been already taken");
    }
}