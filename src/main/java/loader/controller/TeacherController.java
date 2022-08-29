package loader.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import loader.entity.Language;
import loader.repository.VariantRepository;
import loader.service.UserService;
import loader.service.TeacherService;

import loader.entity.User;

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
    VariantRepository variantRepository;

    public TeacherController(UserService userService,
                             TeacherService teacherService,
                             VariantRepository variantRepository)
    {
        this.userService = userService;
        this.teacherService = teacherService;
        this.variantRepository = variantRepository;
    }

    @GetMapping
    public String index(Model model){
        User teacher = userService.getUserByUsername(getUsername());
        model.addAttribute("exams", teacher.getExams());

        ArrayList<Long> list = new ArrayList<>();
        list.add((long)0);
        list.add(teacher.getId());

        model.addAttribute("variantsEN", variantRepository.findAllByVariantOwnerInAndVariantLanguage(list, Language.en));
        model.addAttribute("variantsDE", variantRepository.findAllByVariantOwnerInAndVariantLanguage(list, Language.de));
        model.addAttribute("variantsFR", variantRepository.findAllByVariantOwnerInAndVariantLanguage(list, Language.fr));
        model.addAttribute("variantsES", variantRepository.findAllByVariantOwnerInAndVariantLanguage(list, Language.es));
        return "teacherPage";
    }

    @PostMapping("/newExam")
    public ModelAndView newExam(@RequestParam String datePicker, @RequestParam String examName, @RequestParam(value = "variantId") List<Long> variantsId) throws ParseException, IOException, MessagingException {
        teacherService.createExam(datePicker, examName, variantsId, getUsername());
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