package loader.service;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import loader.entity.*;
import loader.custom.VariantForm;
import loader.custom.ConfigProperties;
import loader.exception.VariantNameExists;
import loader.repository.AudioFileRepository;
import loader.repository.ExamRepository;
import loader.repository.UserRepository;
import loader.repository.VariantRepository;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.passay.EnglishCharacterData;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class TeacherService {

    ExamRepository examRepository;
    UserRepository userRepository;
    ConfigProperties configProperties;
    VariantRepository variantRepository;
    AudioFileRepository audioFileRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator = new PasswordGenerator();
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private final CharacterData digitChars = EnglishCharacterData.Digit;
    private final CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
    private final CharacterData upperCaseChars = EnglishCharacterData.UpperCase;

    private final CharacterRule digitRule = new CharacterRule(digitChars, 4);
    private final CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars, 4);
    private final CharacterRule upperCaseRule = new CharacterRule(upperCaseChars, 4);


    public TeacherService(JavaMailSender mailSender,
                          ExamRepository examRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          ConfigProperties configProperties,
                          VariantRepository variantRepository,
                          AudioFileRepository audioFileRepository,
                          TemplateEngine templateEngine)
    {
        this.mailSender = mailSender;
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.configProperties = configProperties;
        this.variantRepository = variantRepository;
        this.audioFileRepository = audioFileRepository;
        this.templateEngine = templateEngine;
    }

    public void createExam(String date, String username, String examTheme) throws ParseException, IOException, MessagingException {
        User teacher = userRepository.findUserByUsername(username).get();

        Date parsedDate = format.parse(date);
        String examName = teacher.getUsername() + "_" + examTheme;
        String password = passwordGenerator.generatePassword(12, lowerCaseRule, upperCaseRule, digitRule);

        int numberOfExams = examRepository.countExamByExamNameStartingWith(examName);
        if(numberOfExams > 0) {
            examName = teacher.getUsername() + "_" + examTheme + "_" + numberOfExams;
        }

        String packageLink = configProperties.getExamStorage() + "/" + examName;
        Files.createDirectories(Paths.get(packageLink));

        Exam exam = new Exam(
                teacher,
                parsedDate,
                examName,
                passwordEncoder.encode(password),
                UserRole.EXAM,
                packageLink
        );
        examRepository.save(exam);

        MimeMessage message = mailSender.createMimeMessage();
        String body = buildNewExamMail(examName, password);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(teacher.getEmail());
        helper.setFrom("info@speaking-ege.ru");
        helper.setSubject("Exam on " + date);
        helper.setText(body, true);
        try {
            mailSender.send(message);
        }
        catch (Exception exception){
            deleteExam(exam.getId());
        }
    }

    private String buildNewExamMail(String login, String password) {
        Context context = new Context();
        context.setVariable("login", login);
        context.setVariable("password", password);
        context.setVariable("dateString","Message sent: " + new Date());
        return templateEngine.process("newExamMail", context);
    }

    public void createVariant(VariantForm variantForm) throws IOException, VariantNameExists {

        if(variantRepository.existsByVariantName(variantForm.getVariantName())){
            throw new VariantNameExists();
        }

        String editedTaskText1 = variantForm.getTaskText1().replaceAll("<span class=\"ql-cursor\">\uFEFF</span>", "");
        String editedTaskText2 = variantForm.getTaskText2().replaceAll("<span class=\"ql-cursor\">\uFEFF</span>", "");
        String editedTaskText3 = variantForm.getTaskText3().replaceAll("<span class=\"ql-cursor\">\uFEFF</span>", "");
        String editedTaskText4 = variantForm.getTaskText4().replaceAll("<span class=\"ql-cursor\">\uFEFF</span>", "");

        variantForm.setTaskText1(editedTaskText1);
        variantForm.setTaskText2(editedTaskText2);
        variantForm.setTaskText3(editedTaskText3);
        variantForm.setTaskText4(editedTaskText4);

        String packageLink = configProperties.getVariantStorage() + "/" + (int) variantRepository.count();
        Files.createDirectories(Paths.get(packageLink));

        String photo1Path = System.getProperty("user.dir") + "/" + packageLink + "/" + variantForm.getTaskFile2().getOriginalFilename();
        String audioPath = System.getProperty("user.dir") + "/" + packageLink + "/" + variantForm.getTaskFile3().getOriginalFilename();
        String photo2Path = System.getProperty("user.dir") + "/" + packageLink + "/" + variantForm.getTaskFiles4()[0].getOriginalFilename();
        String photo3Path = System.getProperty("user.dir") + "/" + packageLink + "/" + variantForm.getTaskFiles4()[1].getOriginalFilename();

        File photo1 = new File(photo1Path);
        File audio = new File(audioPath);
        File photo2 = new File(photo2Path);
        File photo3 = new File(photo3Path);

        variantForm.getTaskFile2().transferTo(photo1);
        variantForm.getTaskFile3().transferTo(audio);
        variantForm.getTaskFiles4()[0].transferTo(photo2);
        variantForm.getTaskFiles4()[1].transferTo(photo3);

        Variant variant = new Variant(
                variantForm.getTaskText1(),
                variantForm.getTaskText2(),
                variantForm.getTaskText3(),
                variantForm.getTaskText4(),
                audioPath,
                photo1Path,
                photo2Path,
                photo3Path,
                variantForm.getVariantName());

        variantRepository.save(variant);
    }

    public void deleteExam(Long examId) {
        Optional<Exam> examOptional = examRepository.findById(examId);

        if(examOptional.isPresent()){
            Exam exam = examOptional.get();
            String path = exam.getPackageLink();

            audioFileRepository.deleteAll(exam.getAudioFiles());

            try {
                FileSystemUtils.deleteRecursively(Paths.get(path));
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            examRepository.deleteById(examId);
        }
    }

    public ResponseEntity<ByteArrayResource> downloadAudio(String stringPath) throws IOException {

        File file = new File(stringPath);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(stringPath);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public void deleteExams(List<Long> examsId) {
        examsId.forEach(this::deleteExam);
    }
}