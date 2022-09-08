package loader.controller;

import java.io.IOException;
import java.util.Optional;

import ws.schild.jave.EncoderException;
import loader.repository.VariantRepository;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

import loader.entity.Variant;
import loader.service.VariantService;
import loader.service.SpeakingService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/speaking")
public class SpeakingController {

    VariantService variantService;
    SpeakingService speakingService;
    VariantRepository variantRepository;

    public SpeakingController(VariantService variantService,
                              SpeakingService speakingService,
                              VariantRepository variantRepository)
    {
        this.variantService = variantService;
        this.speakingService = speakingService;
        this.variantRepository = variantRepository;
    }

    @GetMapping
    public ModelAndView index(Long variantId, Model model){
        Optional<Variant> variantOptional = variantRepository.findById(variantId);

        if(variantOptional.isEmpty()){
            return new ModelAndView("redirect:/variant");
        }
        model.addAttribute("variant", variantOptional.get());
        return new ModelAndView("speakingPage");
    }

    @PostMapping
    @ResponseBody
    public String receiveAudio(@RequestParam MultipartFile audio) throws IOException, EncoderException {
        speakingService.saveAudio(audio, getUsername());
        return "success";
    }

    private String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserDetails)principal).getUsername();
    }
}