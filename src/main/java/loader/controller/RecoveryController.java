package loader.controller;

import javax.mail.MessagingException;

import loader.service.RecoveryService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recovery")
public class RecoveryController {

    private final RecoveryService recoveryService;
    public RecoveryController(RecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    @PostMapping
    public String recovery(String email) throws MessagingException {
        recoveryService.sendPassword(email);
        return "loginPage";
    }
}