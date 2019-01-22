package pl.coderslab.models;

        import lombok.Getter;
        import lombok.Setter;
        import org.hibernate.validator.constraints.NotBlank;

        import javax.persistence.*;
        import javax.validation.constraints.Min;
        import javax.validation.constraints.NotNull;
        import javax.validation.constraints.Pattern;
        import javax.validation.constraints.Size;
        import java.security.Timestamp;
        import java.time.LocalDate;
        import java.util.Date;
        import java.util.HashSet;
        import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1)
    private Long numberOfBags;

    @NotNull
    @ManyToMany (fetch = FetchType.EAGER)
    Set<Gift> gifts = new HashSet<>();

    @NotNull
    @ManyToMany (fetch = FetchType.EAGER)
    Set <City> cities;

    @NotNull
    @ManyToMany (fetch = FetchType.EAGER)
    Set <Receiver> receivers;

    @NotNull
    @ManyToMany (fetch = FetchType.EAGER)
    Set <Organization> organizations;

    @NotBlank
    @Size(max=80)
    private String streetOfCollection;

    @NotBlank
    @Size(max=50)
    private String cityOfCollection;

    @NotBlank
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$")
    protected String postalCodeOfCollection;

    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$")
    private String telephone;

    @NotNull
    private Date dateOfCollection;

    @Size(max=200)
    private String commentForCourier;

    private LocalDate created;

    private boolean sent = false;

    private boolean opened = false;

    private boolean received = false;

    @ManyToOne(fetch = FetchType.EAGER)
    User user;

}
