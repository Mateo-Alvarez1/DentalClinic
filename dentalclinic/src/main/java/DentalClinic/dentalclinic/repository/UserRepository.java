package DentalClinic.dentalclinic.repository;

import DentalClinic.dentalclinic.repository.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User , Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
