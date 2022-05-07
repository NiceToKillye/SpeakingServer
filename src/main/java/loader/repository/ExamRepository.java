package loader.repository;

import loader.entity.Exam;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findExamByExamName(String examName);
    int countExamByExamNameStartingWith(String examName);
}