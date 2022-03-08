package loader.controller;

import loader.custom.Counter;
import loader.service.VariantService;
import loader.repository.VariantRepository;

import java.io.IOException;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testVariants")
public class TestVariantsController {

    private final VariantRepository variantRepository;
    private final VariantService variantService;

    public TestVariantsController(VariantService variantService,
                                  VariantRepository variantRepository)
    {
        this.variantService = variantService;
        this.variantRepository = variantRepository;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("counter", new Counter());
        model.addAttribute("counterForDelete", new Counter());
        model.addAttribute("variants", variantRepository.findAll());
        return "testVariantsPage";
    }

    @PostMapping
    public ModelAndView deleteVariant(Long variantId) throws IOException {
        variantService.deleteVariant(variantId);
        return new ModelAndView("redirect:/testVariants");
    }

}