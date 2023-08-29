package DentalClinic.dentalclinic.model;

import DentalClinic.dentalclinic.repository.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {
    private String firstname;
    private String surname;
    private String email;
    private String password;
    private Address address;
}
