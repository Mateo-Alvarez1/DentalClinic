package DentalClinic.dentalclinic.model;


import DentalClinic.dentalclinic.repository.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    private String token;
    private String role;
}
