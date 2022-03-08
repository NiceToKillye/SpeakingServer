package loader.entity;

import javax.persistence.*;
import java.util.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "User",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "user_login_email_unique",
                columnNames = {"username", "email"})
        }
)
@Entity(name = "User")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private long id;

    @Column(
            name = "username",
            nullable = false
    )
    private String username;

    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @Column(
            name = "email",
            nullable = false
    )
    private String email;

    @Column(
            name = "enabled",
            nullable = false
    )
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false
    )
    private UserRole userRole;

    @OneToMany(mappedBy="teacher")
    private Set<Exam> exams;


    public User(String email,
                String username,
                String password,
                Boolean enabled,
                UserRole userRole) {

        this.email = email;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", userRole=" + userRole +
                ", exams=" + exams +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole.name());
        return Collections.singleton(authority);
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
        return enabled;
    }
}