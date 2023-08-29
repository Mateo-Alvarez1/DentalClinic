package DentalClinic.dentalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DentistDto {
    private String firstname;
    private String surname;
    private String email;
    private String password;
    private String license;
}
