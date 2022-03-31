package loader.entity;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "Variant")
@Entity(name = "Variant")
public class Variant {

    @Id
    @SequenceGenerator(
            name = "variant_sequence",
            sequenceName = "variant_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "variant_sequence"
    )
    private long id;

    @Column(
            name = "variantName",
            nullable = false
    )
    private String variantName;

    @Column(
            name = "taskText1",
            columnDefinition = "MEDIUMTEXT",
            nullable = false
    )
    private String taskText1;

    @Column(
            name = "taskText2",
            columnDefinition = "MEDIUMTEXT",
            nullable = false
    )
    private String taskText2;

    @Column(
            name = "taskText3",
            columnDefinition = "MEDIUMTEXT",
            nullable = false
    )
    private String taskText3;

    @Column(
            name = "taskText4",
            columnDefinition = "MEDIUMTEXT",
            nullable = false
    )
    private String taskText4;

    @Column(
            name = "photoLink1",
            nullable = false
    )
    private String photoLink1;

    @Column(
            name = "photoLink2",
            nullable = false
    )
    private String photoLink2;

    @Column(
            name = "photoLink3",
            nullable = false
    )
    private String photoLink3;

    @Column(
            name = "audioLink",
            nullable = false
    )
    private String audioLink;

    public Variant(String taskText1,
                   String taskText2,
                   String taskText3,
                   String taskText4,
                   String audioLink,
                   String photoLink1,
                   String photoLink2,
                   String photoLink3,
                   String variantName)
    {
        this.taskText1 = taskText1;
        this.taskText2 = taskText2;
        this.taskText3 = taskText3;
        this.taskText4 = taskText4;
        this.audioLink = audioLink;
        this.photoLink1 = photoLink1;
        this.photoLink2 = photoLink2;
        this.photoLink3 = photoLink3;
        this.variantName = variantName;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "id=" + id +
                ", variantName='" + variantName + '\'' +
                ", taskText1='" + taskText1 + '\'' +
                ", taskText2='" + taskText2 + '\'' +
                ", taskText3='" + taskText3 + '\'' +
                ", taskText4='" + taskText4 + '\'' +
                ", photoLink1='" + photoLink1 + '\'' +
                ", photoLink2='" + photoLink2 + '\'' +
                ", photoLink3='" + photoLink3 + '\'' +
                ", audioLink='" + audioLink + '\'' +
                '}';
    }
}
