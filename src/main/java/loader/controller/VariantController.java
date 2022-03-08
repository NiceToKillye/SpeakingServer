package loader.controller;

import loader.custom.Counter;
import org.springframework.ui.Model;
import loader.service.VariantService;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/variant")
public class VariantController {

    VariantService variantService;
    public VariantController(VariantService variantService){
        this.variantService = variantService;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("variants", variantService.getAllVariants());
        model.addAttribute("counter", new Counter());
        return "variantPage";
    }
}
