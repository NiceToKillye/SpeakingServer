package loader.controller;

import loader.entity.User;
import loader.entity.UserRole;
import loader.repository.UserRepository;
import loader.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        ArrayList<User> teachers = userRepository.findAllByUserRole(UserRole.TEACHER);
        model.addAttribute("teachers", teachers);
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
}