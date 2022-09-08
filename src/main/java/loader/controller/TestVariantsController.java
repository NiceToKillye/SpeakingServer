package loader.controller;

import java.util.List;
import java.io.IOException;

import loader.entity.User;
import loader.entity.Language;

import loader.custom.VariantForm;
import loader.service.VariantService;
import loader.exception.VariantNameExists;
import loader.repository.VariantRepository;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/testVariants")
public class TestVariantsController {

    private final VariantService variantService;
    private final VariantRepository variantRepository;

    public TestVariantsController(VariantService variantService,
                                  VariantRepository variantRepository)
    {
        this.variantService = variantService;
        this.variantRepository = variantRepository;
    }

    @GetMapping
    public String index(){
        return "languageSelectionPage";
    }

    @GetMapping("/{lang:[a-z]{2}}")
    public String variants(Model model, @PathVariable Language lang) {
        model.addAttribute("variants", variantRepository.findAllByVariantOwnerAndVariantLanguage(getUserId(), lang));
        return "testVariantsPage";
    }

    @PostMapping("/newVariant")
    public ModelAndView newVariant(@ModelAttribute VariantForm variantForm)
            throws IOException, VariantNameExists
    {
        variantService.createVariant(variantForm, getUserId());
        return new ModelAndView("redirect:/testVariants");
    }

    @PostMapping("/{lang:[a-z]{2}}/delete")
    public ModelAndView deleteVariant(@RequestParam(value = "variantId") List<Long> variantId,
                                      @PathVariable Language lang)
    {
        variantService.deleteVariants(variantId);
        return new ModelAndView("redirect:/testVariants/" + lang);
    }

    @PostMapping("/{lang:[a-z]{2}}/share")
    public ModelAndView shareVariant(@RequestParam(value = "variantId") List<Long> variantId,
                                     @PathVariable Language lang)
    {
        variantService.shareVariants(variantId);
        return new ModelAndView("redirect:/testVariants/" + lang);
    }

    private long getUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((User) principal).getId();
    }
}