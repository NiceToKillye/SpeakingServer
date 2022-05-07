package loader.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import loader.exception.VariantNameExists;
import loader.service.UserService;
import loader.service.TeacherService;

import loader.entity.User;
import loader.custom.VariantForm;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.mail.MessagingException;

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
    public ModelAndView newExam(@RequestParam String datePicker, @RequestParam String examName) throws ParseException, IOException, MessagingException {
        teacherService.createExam(datePicker, getUsername(), examName);
        return new ModelAndView("redirect:/teacher");
    }

    @PostMapping("/deleteExam")
    public ModelAndView deleteExam(@RequestParam(value = "examId") List<Long> examsId) {
        teacherService.deleteExams(examsId);
        return new ModelAndView("redirect:/teacher");
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadAudio(String audioPath) throws IOException {
        return teacherService.downloadAudio(audioPath);
    }

    private String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserDetails)principal).getUsername();
    }
}