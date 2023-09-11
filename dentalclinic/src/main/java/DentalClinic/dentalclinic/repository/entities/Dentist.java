package DentalClinic.dentalclinic.repository.entities;

import DentalClinic.dentalclinic.model.DentistDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Dentists")
public class Dentist{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column()
    private String license;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Appointment> appointments = new HashSet<>();

    public Dentist(String firstname, String surname, String email, String license) {
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.license = license;
    }

    public Dentist(String firstname, String surname, String email, String password, String license, DentistDto dentistDto) {

    }

    public Dentist(long id, String firstname, String surname, String email, String password, String license) {

    }
}
