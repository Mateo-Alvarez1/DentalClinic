package DentalClinic.dentalclinic.repository;

import DentalClinic.dentalclinic.repository.entities.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface PatientRepository extends JpaRepository<Patient , Long> {
    Optional<Patient> findByEmail(String email);
    boolean existsByEmail(String email);
}
