package loader.controller;

import java.util.List;
import java.util.ArrayList;

import loader.entity.User;
import loader.entity.UserRole;

import loader.repository.UserRepository;
import loader.repository.VariantRepository;

import loader.service.AdminService;
import loader.service.VariantService;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/admin")
public class AdminController {

    AdminService adminService;
    UserRepository userRepository;
    VariantService variantService;
    VariantRepository variantRepository;

    public AdminController(AdminService adminService,
                           UserRepository userRepository,
                           VariantService variantService,
                           VariantRepository variantRepository)
    {
        this.adminService = adminService;
        this.userRepository = userRepository;
        this.variantService = variantService;
        this.variantRepository = variantRepository;
    }

    @GetMapping
    public String index(Model model){
        ArrayList<User> teachers = userRepository.findAllByUserRole(UserRole.TEACHER);
        model.addAttribute("teachers", teachers);
        model.addAttribute("variants", variantRepository.findAllByVariantOwner(0));
        return "adminPage";
    }

    @ResponseBody
    @PostMapping("/enable")
    public String enableTeacher(long teacherId){
        adminService.enableTeacher(teacherId);
        return "success";
    }

    @ResponseBody
    @PostMapping("/delete")
    public String deleteTeacher(long teacherId){
        adminService.deleteTeacher(teacherId);
        return "success";
    }

    @PostMapping("/variants/delete")
    public ModelAndView deleteVariant(@RequestParam(value = "variantId") List<Long> variantsId){
        variantService.deleteVariants(variantsId);
        return new ModelAndView("redirect:/admin");
    }
}