package loader.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "AudioFile")
@Entity(name = "AudioFile")
public class AudioFile {

    @Id
    @SequenceGenerator(
            name = "audio_sequence",
            sequenceName = "audio_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "audio_sequence"
    )
    private long id;

    @Column(
            name = "audioName",
            nullable = false
    )
    private String audioName;

    @Column(
            name = "audioPath",
            nullable = false
    )
    private String audioPath;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name="examId",
            nullable=false
    )
    private Exam exam;

    public AudioFile(String audioName, String audioPath, Exam exam) {
        this.audioName = audioName;
        this.audioPath = audioPath;
        this.exam = exam;
    }

    @Override
    public String toString() {
        return "AudioFile{" +
                "id=" + id +
                ", audioName='" + audioName + '\'' +
                ", audioPath='" + audioPath + '\'' +
                ", exam=" + exam +
                '}';
    }
}
