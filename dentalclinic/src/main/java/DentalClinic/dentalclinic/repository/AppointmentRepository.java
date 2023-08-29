package DentalClinic.dentalclinic.repository;
import DentalClinic.dentalclinic.model.DentistDto;
import DentalClinic.dentalclinic.model.PatientDto;
import DentalClinic.dentalclinic.repository.entities.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment , Long> {
    boolean existsByAppointmentDate(LocalDateTime appointmentDate);
    List<Appointment> findAppointmentByPatient(PatientDto patientDto);
    List<Appointment>  findAppointmentByDentist(DentistDto dentistDto);
}
