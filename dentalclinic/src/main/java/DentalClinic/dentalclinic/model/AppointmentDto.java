package DentalClinic.dentalclinic.model;

import DentalClinic.dentalclinic.repository.entities.Dentist;
import DentalClinic.dentalclinic.repository.entities.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Patient patient;
    private Dentist dentist;
    private LocalDateTime appointmentDate;

}
