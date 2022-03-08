package loader.controller;

import loader.exception.EmailWasTakenException;
import loader.exception.LoginWasTakenException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({
            UsernameNotFoundException.class
    })
    public String recovery(Model model, Exception exception){
        String message = "Something went wrong!";

        if(exception instanceof UsernameNotFoundException){
            message = "User with this email was not found!";
        }

        model.addAttribute("show", true);
        model.addAttribute("errorMessage", message);
        return "recoveryPage";
    }

    @ExceptionHandler({
            EmailWasTakenException.class,
            LoginWasTakenException.class
    })
    public String register(Model model, Exception exception){
        String message = "Something went wrong";

        if(exception instanceof EmailWasTakenException){
            message = "Email was taken";
        }
        else if(exception instanceof LoginWasTakenException){
            message = "Login was taken";
        }

        model.addAttribute("show", true);
        model.addAttribute("errorMessage", message);
        return "registerPage";
    }
}
