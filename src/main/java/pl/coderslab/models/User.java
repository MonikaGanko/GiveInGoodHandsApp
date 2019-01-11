package pl.coderslab.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID userUUID = UUID.randomUUID();

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    private String password;

    @Transient
    private String confirmPassword;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    private boolean enabled = true;

    private boolean validated = true;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}