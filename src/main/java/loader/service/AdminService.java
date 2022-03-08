package loader.service;

import java.util.ArrayList;
import java.util.Optional;

import loader.entity.User;
import loader.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void enableTeachers(ArrayList<Long> teachersId){
        ArrayList<User> teachers = new ArrayList<>();
        if(teachersId.isEmpty()){
            throw new NullPointerException("teachersId array is empty");
        }
        teachersId.forEach(teacherId -> {
            Optional<User> optionalTeacher = userRepository.findById(teacherId);

            if(optionalTeacher.isPresent()){
                User teacher = optionalTeacher.get();
                teacher.setEnabled(true);
                teachers.add(teacher);
            }
            else {
                System.out.println("Teacher with id = " + teacherId + " not found");
            }
        });

        userRepository.saveAll(teachers);
    }
}
