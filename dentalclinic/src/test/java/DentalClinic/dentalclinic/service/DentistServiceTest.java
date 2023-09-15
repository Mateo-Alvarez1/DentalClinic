package DentalClinic.dentalclinic.service;
import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.DentistDto;
import DentalClinic.dentalclinic.repository.DentistRepository;
import DentalClinic.dentalclinic.repository.entities.Dentist;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DentistServiceTest {

    @Mock
    private DentistRepository dentistRepository;
    @Mock
    private ObjectMapper mapper;
    private DentistService dentistService;

    @BeforeEach
    void setup(){
        this.dentistService = new DentistService(dentistRepository , mapper);
    }

    @Test
    void createDentist() throws ObjectAlreadyExistException {
        //give
        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";
        var password = "1234";
        var license = "234";

        var dentistDto = new DentistDto(
                firstname  ,
                surname  ,
                email ,
                password ,
                license
        );

        var expectedSavedDentist = new Dentist(firstname , surname  ,email, password, license , dentistDto );


        //when

        when(dentistRepository.save(any())).thenReturn(expectedSavedDentist);
        var responseDentistService = dentistService.createDentist(dentistDto);

        //then
        assertNotNull(responseDentistService);
        assertEquals(expectedSavedDentist.getFirstname(), responseDentistService.getFirstname());

        verify(dentistRepository).save(any());
    }


    @Test
    void willThrownDentistEmailExists()  {

        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";
        var password = "1234";
        var license = "234";

        var dentistDto = new DentistDto(
                firstname  ,
                surname  ,
                email ,
                password ,
                license
        );

        //when
        given(dentistRepository.existsByEmail(dentistDto.getEmail()))
                .willReturn(true);

        //then
        assertThatThrownBy( () -> dentistService.createDentist(dentistDto))
                .isInstanceOf(ObjectAlreadyExistException.class)
                .hasMessageContaining("Dentist already exists in the database");
        verify(dentistRepository , never()).save(any());
    }

    @Test
    void updateDentist() throws ResourceNotFoundException {

        //given
    var id = 1L;
    DentistDto dentistDto = new DentistDto();
    Dentist existingDentist = new Dentist();

    when(dentistRepository.findById(id)).thenReturn(Optional.of(existingDentist));

    Dentist updateDentist = dentistService.updateDentist(dentistDto , id);

    verify(dentistRepository, times(1)).save(existingDentist);


    }

    @Test
    void updateDentistNotFound(){
        Long id = 1L;
        DentistDto dentistDto = new DentistDto();
        when(dentistRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistService.updateDentist(dentistDto , id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dentist not found in the database");
        verify(dentistRepository , never()).save(any());

    }


    @Test
    void deleteDentist() throws ResourceNotFoundException {

        //give

        var id = 1L;
        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";
        var password = "1234";
        var license = "234";

        var dentist = new Dentist(
                id,
                firstname  ,
                surname  ,
                email ,
                password ,
                license
        );

        //then
        doReturn(true).when(dentistRepository).existsById(dentist.getId());
        dentistService.deleteDentist(dentist.getId());

        //when
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(dentistRepository).deleteById(argumentCaptor.capture());
        Long captureId = argumentCaptor.getValue();
        assertThat(captureId).isEqualTo(dentist.getId());
    }


    @Test
    void deleteDentistNoPresent(){


        var id = 1L;
        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";
        var password = "1234";
        var license = "234";

        var dentist = new Dentist(
                id,
                firstname  ,
                surname  ,
                email ,
                password,
                license
        );

        //WHEN
        doReturn(false).when(dentistRepository).existsById(dentist.getId());

        //THEN
        assertThatThrownBy(() -> dentistService.deleteDentist(dentist.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dentist not found in the database");
        verify(dentistRepository , never()).save(any());
    }

    @Test
    void getDentist() throws ResourceNotFoundException {
            Long id = 1L;
            Dentist existingDentist = new Dentist();

            when(dentistRepository.findById(id)).thenReturn(Optional.of(existingDentist));

            Dentist retrievedDentist = dentistService.getDentist(id);

            assertThat(retrievedDentist).isNotNull();
            assertEquals(existingDentist, retrievedDentist);

    }

    @Test
    void getDentistNoPresent(){
        Long id = 1L;
        Dentist existingDentist = new Dentist();

        when(dentistRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistService.getDentist(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dentist not found in the database");

        verify(dentistRepository , never()).save(any());
    }

    @Test
    void getDentistByFirstnameAndSurname() throws ResourceNotFoundException {

        String firstname = "test";
        String surnname = "tested";
        Dentist existingDentist = new Dentist();

        when(dentistRepository.findByFirstnameAndSurname(firstname , surnname)).thenReturn(Optional.of(existingDentist));

        Dentist retrievedDentist = dentistService.getDentistByFirstnameAndSurname(firstname , surnname);

        assertThat(retrievedDentist).isNotNull();
        assertThat(existingDentist).isEqualTo(retrievedDentist);


    }

    @Test
    void notFoundDentistByFirstnameAndSurname() {

        String firstname = "test";
        String surnname = "tested";

        when(dentistRepository.findByFirstnameAndSurname(firstname , surnname)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> dentistService.getDentistByFirstnameAndSurname(firstname,surnname))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dentist not found in the database");
        verify(dentistRepository , never()).save(any());

    }



    @Test
    void getAllDentists() throws ResourceNotFoundException {

        when(dentistRepository.findAll()).thenReturn(Arrays.asList(new Dentist(), new Dentist()));

        Collection<DentistDto> dentistDtos = dentistService.getAllDentists();

        assertNotNull(dentistDtos);
        assertFalse(dentistDtos.isEmpty());

    }

    @Test
    void getListOfDentistIsEmpty() {

        when(dentistRepository.findAll()).thenReturn(Collections.emptyList());


        assertThatThrownBy( () -> dentistService.getAllDentists())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("The list of dentists is empty");

        verify(dentistRepository , never()).save(any());
    }


}