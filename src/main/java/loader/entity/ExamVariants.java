package loader.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "ExamVariants", indexes = @Index(columnList = "examId"))
@Entity(name = "ExamVariants")
public class ExamVariants {

    @Id
    @SequenceGenerator(
            name = "exam_variant_sequence",
            sequenceName = "exam_variant_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exam_variant_sequence"
    )
    private long id;

    @Column(
            name = "variantId",
            nullable = false
    )
    private long variantId;

    @Column(
            name = "examId",
            nullable = false
    )
    private long examId;

    public ExamVariants(long variantId, long examId) {
        this.variantId = variantId;
        this.examId = examId;
    }
}
