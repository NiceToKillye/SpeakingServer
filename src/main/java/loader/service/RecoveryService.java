package loader.service;

import java.util.Optional;

import loader.entity.User;
import loader.repository.UserRepository;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.passay.EnglishCharacterData;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class RecoveryService {

    private final JavaMailSender mailSender;
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final TemplateEngine templateEngine;


    private final PasswordGenerator passwordGenerator = new PasswordGenerator();

    private final CharacterData digitChars = EnglishCharacterData.Digit;
    private final CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
    private final CharacterData upperCaseChars = EnglishCharacterData.UpperCase;

    private final CharacterRule digitRule = new CharacterRule(digitChars, 4);
    private final CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars, 4);
    private final CharacterRule upperCaseRule = new CharacterRule(upperCaseChars, 4);

    public RecoveryService(JavaMailSender mailSender,
                           UserRepository repository,
                           PasswordEncoder encoder,
                           TemplateEngine templateEngine)
    {
        this.mailSender = mailSender;
        this.repository = repository;
        this.encoder = encoder;
        this.templateEngine = templateEngine;
    }

    public void sendPassword(String email) throws MessagingException {
        Optional<User> userList = repository.findUserByEmail(email);

        if(userList.isEmpty()){
            throw new UsernameNotFoundException("User with this email was not found!");
        }

        String password = passwordGenerator.generatePassword(12, lowerCaseRule, upperCaseRule, digitRule);

        MimeMessage message = mailSender.createMimeMessage();
        String body = buildNewPasswordMail(password);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setFrom("info@speaking-ege.ru");
        message.setSubject("New password");
        helper.setText(body, true);

        User user = userList.get();
        user.setPassword(encoder.encode(password));
        repository.save(user);

        mailSender.send(message);
    }

    public String buildNewPasswordMail(String password) {
        Context context = new Context();
        context.setVariable("password", password);
        return templateEngine.process("newPasswordMail", context);
    }
}
