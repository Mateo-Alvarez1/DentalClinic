package DentalClinic.dentalclinic.repository;

import DentalClinic.dentalclinic.repository.entities.Address;
import DentalClinic.dentalclinic.repository.entities.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @AfterEach
    void clearAllTest(){
        patientRepository.deleteAll();
    }

    @Test
    void itShouldPatientExistsEmail(){
        //GIVEN
        String email = "mateo@gmail.com";
        Address address = new Address( "san lorenzo" , "santa lucia" , "San juan" , 1234);
        Patient patient = new Patient(
                "Mateo" ,
                "Alvarez" ,
                email ,
                address
        );
        patientRepository.save(patient);

        //WHEN
        boolean expected = patientRepository.existsByEmail(email);
        //THEN
        assertThat(expected).isTrue();

    }


    @Test
    void itShouldPatientEmailDoesNotExists(){
        //GIVEN
        String email = "mateo@gmail.com";

        //WHEN
        boolean expected = patientRepository.existsByEmail(email);
        //THEN
        assertThat(expected).isFalse();

    }


}