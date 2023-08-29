package DentalClinic.dentalclinic.repository;

import DentalClinic.dentalclinic.repository.entities.Dentist;
import DentalClinic.dentalclinic.repository.entities.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface DentistRepository extends JpaRepository<Dentist, Long> {

    Optional<Dentist> findByEmail(String email);
    Optional<Dentist> findByFirstnameAndSurname(String firstname , String surname);
    boolean existsByEmail(String email);
}
