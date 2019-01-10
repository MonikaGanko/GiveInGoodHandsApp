
package pl.coderslab.models;

        import lombok.Getter;
        import lombok.Setter;
        import org.hibernate.validator.constraints.NotBlank;

        import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String role;
}