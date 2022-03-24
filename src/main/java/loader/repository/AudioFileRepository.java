package loader.repository;

import java.util.List;
import java.util.Optional;

import loader.entity.Exam;
import loader.entity.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
    Optional<List<AudioFile>> findAllByExam(Exam exam);
}
