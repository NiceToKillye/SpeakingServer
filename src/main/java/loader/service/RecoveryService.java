package loader.service;

import java.util.Optional;

import loader.entity.User;
import loader.repository.UserRepository;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.passay.EnglishCharacterData;

import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class RecoveryService {

    private final JavaMailSender mailSender;
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    private final PasswordGenerator passwordGenerator = new PasswordGenerator();

    private final CharacterData digitChars = EnglishCharacterData.Digit;
    private final CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
    private final CharacterData upperCaseChars = EnglishCharacterData.UpperCase;

    private final CharacterRule digitRule = new CharacterRule(digitChars, 4);
    private final CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars, 4);
    private final CharacterRule upperCaseRule = new CharacterRule(upperCaseChars, 4);

    public RecoveryService(JavaMailSender mailSender,
                           UserRepository repository,
                           PasswordEncoder encoder)
    {
        this.mailSender = mailSender;
        this.repository = repository;
        this.encoder = encoder;
    }

    public void sendPassword(String email) {
        Optional<User> userList = repository.findUserByEmail(email);

        if(userList.isEmpty()){
            throw new UsernameNotFoundException("User with this email was not found!");
        }

        String password = passwordGenerator.generatePassword(12, lowerCaseRule, upperCaseRule, digitRule);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("info@speaking-ege.ru");
        message.setSubject("New password");
        message.setText("Your new password: " + password);

        User user = userList.get();
        user.setPassword(encoder.encode(password));
        repository.save(user);

        mailSender.send(message);
        System.out.println(password);
    }
}
