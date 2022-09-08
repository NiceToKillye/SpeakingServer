package loader.service;

import java.util.Optional;
import java.io.IOException;
import java.nio.file.Paths;

import loader.entity.User;
import loader.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

@Service
public class AdminService {

    UserRepository userRepository;
    ExamRepository examRepository;
    VariantRepository variantRepository;
    AudioFileRepository audioFileRepository;
    ExamVariantsRepository examVariantsRepository;

    public AdminService(UserRepository userRepository,
                        ExamRepository examRepository,
                        VariantRepository variantRepository,
                        AudioFileRepository audioFileRepository,
                        ExamVariantsRepository examVariantsRepository)
    {
        this.userRepository = userRepository;
        this.examRepository = examRepository;
        this.variantRepository = variantRepository;
        this.audioFileRepository = audioFileRepository;
        this.examVariantsRepository = examVariantsRepository;
    }

    public void enableTeacher(long teacherId){
        Optional<User> optionalTeacher = userRepository.findById(teacherId);

        if(optionalTeacher.isPresent()){
            User teacher = optionalTeacher.get();
            teacher.setEnabled(true);
            userRepository.save(teacher);
        }
    }

    public void deleteTeacher(long teacherId){
        Optional<User> optionalTeacher = userRepository.findById(teacherId);

        if(optionalTeacher.isPresent()){
            User teacher = optionalTeacher.get();

            teacher.getExams().forEach(exam -> {
                audioFileRepository.deleteAll(exam.getAudioFiles());
                try {
                    FileSystemUtils.deleteRecursively(Paths.get(exam.getPackageLink()));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            examRepository.deleteAll(teacher.getExams());
            userRepository.delete(teacher);
        }
    }
}