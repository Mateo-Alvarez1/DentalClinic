package DentalClinic.dentalclinic.repository;
import DentalClinic.dentalclinic.repository.entities.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void itShouldAppointmentExistsByDate(){
        //GIVEN
        LocalDateTime dateTime = LocalDateTime.of(2022, 8, 24, 15, 30, 45, 789_000_000);
        Appointment appointment = new Appointment(
                dateTime
        );
        appointmentRepository.save(appointment);

        //WHEN
        boolean expected = appointmentRepository.existsByAppointmentDate(dateTime);

        //THEN
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldAppointmentByDateDoesNotExists(){
        //GIVEN
        LocalDateTime dateTime = LocalDateTime.of(2022, 8, 24, 15, 30, 45, 789_000_000);

        //WHEN
        boolean expected = appointmentRepository.existsByAppointmentDate(dateTime);

        //THEN
        assertThat(expected).isFalse();
    }


}