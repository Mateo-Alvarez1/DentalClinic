package DentalClinic.dentalclinic.repository.entities;

import DentalClinic.dentalclinic.model.PatientDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Patients")
public class Patient{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id" , referencedColumnName = "id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Appointment> appointments = new HashSet<>();

    public Patient( String firstname, String surname, String email, Address address) {
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.address = address;
    }


    public Patient(String firstname, String surname, String email, String unknown, PatientDto patientDto) {
    }

    public Patient(long id, String firstname, String surname, String email, Address address) {

    }


}
