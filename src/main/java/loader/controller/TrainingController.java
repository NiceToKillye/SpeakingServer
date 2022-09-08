package loader.controller;

import java.util.Optional;

import loader.entity.Variant;
import loader.entity.Language;

import org.springframework.ui.Model;
import loader.repository.VariantRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/training")
public class TrainingController {

    VariantRepository variantRepository;

    public TrainingController(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    @GetMapping
    public String index(){
        return "training";
    }

    @GetMapping("/{lang:[a-z]{2}}")
    public String variants(Model model, @PathVariable Language lang) {
        model.addAttribute("variants", variantRepository.findAllByVariantOwnerAndVariantLanguage(0, lang));
        return "trainingVariants";
    }

    @GetMapping("/recording")
    public ModelAndView recording(long variantId){
        return new ModelAndView("redirect:/training/recording/" + variantId);
    }

    @GetMapping("/recording/{variantId}")
    public ModelAndView recordingVariant(Model model, @PathVariable long variantId){
        Optional<Variant> variantOptional = variantRepository.findById(variantId);

        if(variantOptional.isEmpty()) {
            return new ModelAndView("redirect:/training");
        }

        model.addAttribute("variant", variantOptional.get());
        return new ModelAndView("trainingRecording");
    }
}