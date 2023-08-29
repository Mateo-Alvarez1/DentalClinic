package DentalClinic.dentalclinic.model;

import DentalClinic.dentalclinic.repository.entities.Dentist;
import DentalClinic.dentalclinic.repository.entities.Patient;
import DentalClinic.dentalclinic.repository.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private String accessToken;
    private String token;
    private Patient patient;
    private Dentist dentist;
    private Role role;

}
