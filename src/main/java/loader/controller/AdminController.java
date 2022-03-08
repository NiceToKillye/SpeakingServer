package loader.controller;

import loader.custom.AdminForm;
import loader.entity.User;
import loader.repository.UserRepository;
import loader.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    AdminService adminService;
    UserRepository userRepository;

    public AdminController(AdminService adminService,
                           UserRepository userRepository)
    {
        this.adminService = adminService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String index(Model model){
        ArrayList<User> teachers = userRepository.findAllByEnabled(false);
        model.addAttribute("teachers", teachers);
        return "adminPage";
    }

    @PostMapping
    public ModelAndView enableTeachers(AdminForm adminForm){
        adminService.enableTeachers(adminForm.getTeachersId());
        return new ModelAndView("redirect:/admin");
    }
}