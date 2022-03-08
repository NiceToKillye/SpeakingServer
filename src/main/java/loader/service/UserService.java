package loader.service;

import loader.entity.User;
import loader.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow();
    }
}