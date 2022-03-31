package loader.controller;

import java.io.IOException;
import java.text.ParseException;

import loader.exception.VariantNameExists;
import loader.service.UserService;
import loader.service.TeacherService;

import loader.entity.User;
import loader.custom.VariantForm;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    UserService userService;
    TeacherService teacherService;

    public TeacherController(UserService userService,
                             TeacherService teacherService)
    {
        this.userService = userService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String index(Model model){
        User teacher = userService.getUserByUsername(getUsername());
        model.addAttribute("exams", teacher.getExams());
        return "teacherPage";
    }

    @PostMapping("/newVariant")
    public ModelAndView newVariant(@ModelAttribute VariantForm variantForm) throws IOException, VariantNameExists {
        teacherService.createVariant(variantForm);
        return new ModelAndView("redirect:/teacher");
    }

    @PostMapping("/newExam")
    public ModelAndView newExam(@RequestBody String datePicker) throws ParseException, IOException {
        teacherService.createExam(datePicker.substring(11), getUsername());
        return new ModelAndView("redirect:/teacher");
    }

    @PostMapping("/deleteExam")
    public ModelAndView deleteExam(Long examId) throws IOException {
        teacherService.deleteExam(examId);
        return new ModelAndView("redirect:/teacher");
    }

    private String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserDetails)principal).getUsername();
    }
}