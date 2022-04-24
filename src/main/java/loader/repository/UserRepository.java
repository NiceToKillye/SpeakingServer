package loader.repository;

import loader.entity.User;

import java.util.Optional;
import java.util.ArrayList;

import loader.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    ArrayList<User> findAllByEnabled(boolean enabled);
    ArrayList<User> findAllByUserRole(UserRole userRole);
    Optional<User> findUserByUsername(String username);
}