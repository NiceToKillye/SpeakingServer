package loader.controller;

import java.io.IOException;
import it.sauronsoftware.jave.*;
import loader.service.SpeakingService;
import org.springframework.ui.Model;

import loader.entity.Variant;
import loader.service.VariantService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/speaking")
public class SpeakingController {

    VariantService variantService;
    SpeakingService speakingService;

    public SpeakingController(VariantService variantService, SpeakingService speakingService) {
        this.variantService = variantService;
        this.speakingService = speakingService;
    }

    @GetMapping
    public ModelAndView index(Long variantId, Model model){
        Variant variant;
        try{
            variant = variantService.getVariantById(variantId);
        }
        catch (NullPointerException exception){
            return new ModelAndView("redirect:/variant");
        }
        model.addAttribute("variant", variant);
        return new ModelAndView("/speakingPage");
    }


    @PostMapping
    @ResponseBody
    public String receiveAudio(@RequestParam MultipartFile audio) throws IOException, EncoderException {
        speakingService.saveAudio(audio);
        return "success";
    }
}