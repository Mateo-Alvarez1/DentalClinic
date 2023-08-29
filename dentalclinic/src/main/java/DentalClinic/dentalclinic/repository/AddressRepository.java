package DentalClinic.dentalclinic.repository;

import DentalClinic.dentalclinic.repository.entities.Address;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address , Long> {
}
