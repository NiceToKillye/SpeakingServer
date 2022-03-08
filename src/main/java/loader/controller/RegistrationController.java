package loader.controller;

import loader.custom.RegistrationForm;
import loader.service.RegistrationService;

import loader.exception.EmailWasTakenException;
import loader.exception.LoginWasTakenException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    private String register(RegistrationForm registrationForm)
            throws
            LoginWasTakenException,
            EmailWasTakenException
    {
        registrationService.register(registrationForm);
        return "loginPage";
    }

}
