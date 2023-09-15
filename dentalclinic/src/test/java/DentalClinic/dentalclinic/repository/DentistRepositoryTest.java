package DentalClinic.dentalclinic.repository;

import DentalClinic.dentalclinic.repository.entities.Dentist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class DentistRepositoryTest {

    @Autowired
    private DentistRepository dentistRepository;

    @Test
    void itShouldDentistExistsEmail(){
        //GIVE
        String email = "dentis@email.com";
        Dentist dentist = new Dentist(
                "Carlos" ,
                "Bustamante",
                email,
                "1234"
        );
        dentistRepository.save(dentist);
        //WHEN
        boolean expected = dentistRepository.existsByEmail(email);
        //THEN
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldDentistEmailDoesntExists(){
        //GIVE
        String email = "dentis@email.com";
        //WHEN
        boolean expected = dentistRepository.existsByEmail(email);
        //THEN
        assertThat(expected).isFalse();
    }


}