package loader.controller;


import loader.entity.Exam;
import loader.entity.Variant;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Set;

@NoArgsConstructor
@Controller
@RequestMapping("/variant")
public class VariantController {

    @GetMapping
    public String index(Model model){
        model.addAttribute("variants", getExamVariants());
        return "variantPage";
    }

    private Set<Variant> getExamVariants(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((Exam) principal).getVariants();
    }
}
