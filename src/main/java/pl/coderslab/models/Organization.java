package pl.coderslab.models;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String goalAndMission;

    @NotNull
    @ManyToMany (fetch = FetchType.EAGER)
    Set<Receiver> receivers;

    @NotBlank
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$")
    protected String postalCode;

    @NotNull
    @ManyToMany (fetch = FetchType.EAGER)
    Set<City> cities;

    @NotBlank
    @Size(max=80)
    private String street;

    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$")
    private String telephone;
}
