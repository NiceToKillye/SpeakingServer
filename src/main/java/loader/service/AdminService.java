package loader.service;

import java.util.Optional;
import java.io.IOException;
import java.nio.file.Paths;

import loader.entity.User;
import loader.repository.ExamRepository;
import loader.repository.UserRepository;
import loader.repository.AudioFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

@Service
public class AdminService {

    UserRepository userRepository;
    ExamRepository examRepository;
    AudioFileRepository audioFileRepository;

    public AdminService(UserRepository userRepository,
                        ExamRepository examRepository,
                        AudioFileRepository audioFileRepository)
    {
        this.userRepository = userRepository;
        this.examRepository = examRepository;
        this.audioFileRepository = audioFileRepository;
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