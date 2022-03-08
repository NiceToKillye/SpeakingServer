package loader.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String loadLogin(){
        return "loginPage";
    }

    @GetMapping("recovery")
    public String loadRecovery(Model model){
        model.addAttribute("show", false);
        return "recoveryPage";
    }

    @GetMapping("registration")
    public String loadRegistration(Model model){
        model.addAttribute("show", false);
        return "registerPage";
    }
}
