package loader.repository;

import loader.entity.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
}
