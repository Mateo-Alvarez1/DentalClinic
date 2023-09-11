package DentalClinic.dentalclinic.repository;
import DentalClinic.dentalclinic.repository.entities.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;

@Repository
@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment , Long> {
    boolean existsByAppointmentDate(LocalDateTime appointmentDate);
    Collection<Appointment> findAppointmentByPatientId(Long patientId);
    Collection<Appointment>  findAppointmentByDentistId(Long dentistId);
}
