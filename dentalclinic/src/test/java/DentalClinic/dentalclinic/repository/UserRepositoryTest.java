package DentalClinic.dentalclinic.repository;
import DentalClinic.dentalclinic.repository.entities.Role;
import DentalClinic.dentalclinic.repository.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clearTests(){
        userRepository.deleteAll();
    }
    @Test
    void itShouldUserExistsEmail(){
        //GIVEN
        String email = "mateo@alvarez.com";
        User user = new User(
                "Mateo" ,
                "Alvarez" ,
                 email,
                "12234" ,
                Role.ROLE_USER
        );
        userRepository.save(user);
        //WHEN
        boolean expected = userRepository.existsByEmail(email);
        //THEN
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldUserEmailDoesNotExists(){
        //GIVEN
        String email = "mateo@alvarez.com";

        //WHEN
        boolean expected = userRepository.existsByEmail(email);
        //THEN
        assertThat(expected).isFalse();
    }


}