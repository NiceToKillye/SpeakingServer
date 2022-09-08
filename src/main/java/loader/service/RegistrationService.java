package loader.service;

import loader.entity.User;
import loader.custom.RegistrationForm;
import loader.repository.ExamRepository;
import loader.repository.UserRepository;
import static loader.entity.UserRole.TEACHER;
import loader.exception.EmailWasTakenException;
import loader.exception.LoginWasTakenException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserRepository repository;
    private final ExamRepository examRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository repository,
                               ExamRepository examRepository,
                               PasswordEncoder passwordEncoder)
    {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.examRepository = examRepository;
    }

    public void register(RegistrationForm registrationForm)
            throws
            EmailWasTakenException,
            LoginWasTakenException
    {
        if(repository.findUserByUsername(registrationForm.getUsername()).isPresent() ||
                examRepository.findExamByExamName(registrationForm.getUsername()).isPresent()){
            throw new LoginWasTakenException(registrationForm.getUsername());
        }
        else if (repository.findUserByEmail(registrationForm.getEmail()).isPresent()){
            throw new EmailWasTakenException(registrationForm.getEmail());
        }

        User user = new User(
                registrationForm.getEmail(),
                registrationForm.getUsername(),
                passwordEncoder.encode(registrationForm.getPassword()),
                false,
                TEACHER);

        repository.save(user);
    }
}