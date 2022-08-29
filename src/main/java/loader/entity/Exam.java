package loader.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.Date;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "Exam")
@Entity(name = "Exam")
public class Exam implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "exam_sequence",
            sequenceName = "exam_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exam_sequence"
    )
    private long id;

    @Column(
            name = "examName",
            nullable = false
    )
    private String examName;

    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @Column(
            name = "examDate",
            nullable = false
    )
    private Date examDate;

    @Column(
            name = "packageLink",
            nullable = false
    )
    private String packageLink;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false
    )
    private UserRole userRole;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name="teacherId",
            nullable=false
    )
    private User teacher;

    @OneToMany(mappedBy="exam")
    private Set<AudioFile> audioFiles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ExamVariants",
            joinColumns = { @JoinColumn(name = "examId") },
            inverseJoinColumns = { @JoinColumn(name = "variantId") }
    )
    private Set<Variant> variants;

    public Exam(
            User teacher,
            Date examDate,
            String examName,
            String password,
            UserRole userRole,
            String packageLink)
    {
        this.teacher = teacher;
        this.examDate = examDate;
        this.examName = examName;
        this.password = password;
        this.userRole = userRole;
        this.packageLink = packageLink;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", examName='" + examName + '\'' +
                ", password='" + password + '\'' +
                ", examDate=" + examDate +
                ", packageLink='" + packageLink + '\'' +
                ", userRole=" + userRole +
                ", teacher=" + teacher +
                ", audioFiles=" + audioFiles +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole.name());
        return Collections.singleton(authority);
    }

    @Override
    public String getUsername() {
        return examName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}