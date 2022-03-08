package loader.controller;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    SimpleGrantedAuthority authorityExam = new SimpleGrantedAuthority("ROLE_EXAM");
    SimpleGrantedAuthority authorityAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
    SimpleGrantedAuthority authorityTeacher = new SimpleGrantedAuthority("ROLE_TEACHER");

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Collection<? extends GrantedAuthority> authorities = getAuthorities();

        String page = "/";
        String message = "Something went wrong!";
        if (authorities.contains(authorityExam)){
            page+= "variant";
        }
        else if(authorities.contains(authorityAdmin)){
            page+= "admin";
        }
        else if (authorities.contains(authorityTeacher)){
            page+= "teacher";
        }
        else {
            page+= "login";
        }

        model.addAttribute("previousPage", page);
        model.addAttribute("errorMessage", message);
        model.addAttribute("errorCode", status.toString());

        return "errorPage";
    }

    private Collection<? extends GrantedAuthority> getAuthorities(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof String && principal.toString().equals("anonymousUser")){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + "ANON");
            return Collections.singleton(authority);
        }
        assert principal instanceof UserDetails;
        return ((UserDetails)principal).getAuthorities();
    }
}
