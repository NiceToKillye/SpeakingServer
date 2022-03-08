package loader.service;

import java.util.Optional;

import loader.entity.Exam;
import loader.entity.User;
import loader.repository.ExamRepository;
import loader.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class MyDetailService implements UserDetailsService {

    UserRepository userRepository;
    ExamRepository examRepository;

    public MyDetailService(UserRepository userRepository,
                           ExamRepository examRepository)
    {
        this.userRepository = userRepository;
        this.examRepository = examRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(login);
        Optional<Exam> exam = examRepository.findExamByExamName(login);

        if(user.isPresent()){
            return user.orElseThrow();
        }
        else {
            return exam.orElseThrow(() -> new UsernameNotFoundException(login));
        }
    }
}
