package loader.service;

import java.util.Date;
import java.util.Optional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import loader.entity.Exam;
import loader.entity.User;
import loader.entity.Variant;
import loader.entity.UserRole;
import loader.custom.VariantForm;
import loader.custom.ConfigProperties;
import loader.repository.ExamRepository;
import loader.repository.UserRepository;
import loader.repository.VariantRepository;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.passay.EnglishCharacterData;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class TeacherService {

    ExamRepository examRepository;
    UserRepository userRepository;
    ConfigProperties configProperties;
    VariantRepository variantRepository;
    private final JavaMailSender mailSender;

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
                          VariantRepository variantRepository)
    {
        this.mailSender = mailSender;
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.configProperties = configProperties;
        this.variantRepository = variantRepository;
    }

    public void createExam(String date, String username) throws ParseException, IOException {
        User teacher = userRepository.findUserByUsername(username).get();

        Date parsedDate = format.parse(date);
        String examName = teacher.getUsername() + "_" + format.format(parsedDate);
        String password = passwordGenerator.generatePassword(12, lowerCaseRule, upperCaseRule, digitRule);

        int counter = 1;
        while(examRepository.findExamByExamName(examName).isPresent()){
            examName = teacher.getUsername() + "_" + format.format(parsedDate) + "_" + counter;
            counter++;
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

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(teacher.getEmail());
        message.setFrom("info@speaking-ege.ru");
        message.setSubject("Exam on " + date);
        message.setText("Exam date: "     + date     + "\n" +
                        "Exam login: "    + examName + "\n" +
                        "Exam password: " + password);
        mailSender.send(message);
    }

    public void createVariant(VariantForm variantForm) throws IOException {
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
                photo3Path);

        variantRepository.save(variant);
    }

    public void deleteExam(Long examId) throws IOException {
        if (examId == null){
            throw new NullPointerException("examId is null");
        }

        Optional<Exam> examOptional = examRepository.findById(examId);
        if(examOptional.isEmpty()) {
            throw new NullPointerException("Exam with this id = " + examId + " does not exist");
        }

        String path = examOptional.get().getPackageLink();
        System.out.println(path);
        FileSystemUtils.deleteRecursively(Paths.get(path));

        examRepository.deleteById(examId);
    }
}