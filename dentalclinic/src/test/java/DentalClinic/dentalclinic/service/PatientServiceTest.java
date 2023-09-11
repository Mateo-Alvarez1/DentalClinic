package DentalClinic.dentalclinic.service;
import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.PatientDto;
import DentalClinic.dentalclinic.repository.PatientRepository;
import DentalClinic.dentalclinic.repository.entities.Address;
import DentalClinic.dentalclinic.repository.entities.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock private PatientRepository patientRepository;
    @Mock private ObjectMapper mapper;
    private PatientService patientService;

    @BeforeEach
    void setUp(){
        patientService = new PatientService(patientRepository ,mapper);
    }

    @Test
    void createPatient() throws ObjectAlreadyExistException {
        //give
        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";
        var password = "1234";

        var address = new Address( "avenida" , "siempre viva" , "San juan" , 1234);
        var patientDto = new PatientDto(
                firstname  ,
                surname  ,
                email ,
                password ,
                address
        );

        var expectedSavedPatient = new Patient(firstname , surname  ,email, password,  patientDto );


        //when

        when(patientRepository.save(any())).thenReturn(expectedSavedPatient);
        var responsePatientService = patientService.createPatient(patientDto);

        //then
        assertNotNull(responsePatientService);
        assertEquals(expectedSavedPatient .getFirstname(), responsePatientService.getFirstname());

        verify(patientRepository).save(any());
    }

    @Test
    void willThrownPatientEmailExists() {

        //give
        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";
        var password = "1234";

        var address = new Address( "avenida" , "siempre viva" , "San juan" , 1234);
        var patientDto = new PatientDto(
                firstname  ,
                surname  ,
                email ,
                password ,
                address
        );

        //when
        given(patientRepository.existsByEmail(patientDto.getEmail()))
                .willReturn(true);

        //then
        assertThatThrownBy( () -> patientService.createPatient(patientDto))
                .isInstanceOf(ObjectAlreadyExistException.class)
                .hasMessageContaining("Patient already exists in the database");
        verify(patientRepository , never()).save(any());
    }



    @Test
    void updatePatient() throws ResourceNotFoundException {
      Long id = 1L;
      PatientDto patientDto = new PatientDto();
      Patient patient = new Patient();

      when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

      Patient updateDentist = patientService.updatePatient(patientDto , id);

      verify(patientRepository , times(1)).save(patient);

    }


    @Test
    void updatePatientNotFound(){
        Long id = 1L;
        PatientDto patientDto = new PatientDto();

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> patientService.updatePatient(patientDto , id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patient not found in the database");
        verify(patientRepository , never()).save(any());
    }


    @Test
    void deletePatient() throws ResourceNotFoundException {

        var id = 1L;
        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";

        var address = new Address( "avenida" , "siempre viva" , "San juan" , 1234);
        var patient = new Patient(
                id,
                firstname  ,
                surname  ,
                email ,
                address
        );

        //WHEN
        doReturn(true).when(patientRepository).existsById(id);
        patientService.deletePatient(id);

        //THEN
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(patientRepository).deleteById(argumentCaptor.capture());
        Long captureId = argumentCaptor.getValue();
        assertThat(captureId).isEqualTo(id);
    }

    @Test
    void willThrownWhenPatientIsNotPresent(){


        var id = 1L;
        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";

        var address = new Address( "avenida" , "siempre viva" , "San juan" , 1234);
        var patient = new Patient(
                id,
                firstname  ,
                surname  ,
                email ,
                address
        );

        //WHEN
        doReturn(false).when(patientRepository).existsById(patient.getId());

        //THEN
        assertThatThrownBy(() -> patientService.deletePatient(patient.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patient not found in the database");
        verify(patientRepository , never()).save(any());
    }

    @Test
    void getPatient() throws ResourceNotFoundException {

        Long id = 1L;
        Patient existingPatient = new Patient();

        when(patientRepository.findById(id)).thenReturn(Optional.of(existingPatient));

        Patient retrievedPatient = patientService.getPatient(id);

        assertThat(retrievedPatient).isNotNull();
        assertThat(existingPatient).isEqualTo(retrievedPatient);

    }

    @Test
    void getPatientNoPresent(){
        Long id = 1L ;

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> patientService.getPatient(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dentist not found in the database");

        verify(patientRepository , never()).save(any());

    }


    @Test
    void getAllPatients() throws ResourceNotFoundException {

        when(patientRepository.findAll()).thenReturn(Arrays.asList(new Patient(), new Patient()));

        Collection<PatientDto> patientsDtos = patientService.getAllPatients();

        assertNotNull(patientsDtos);
        assertFalse(patientsDtos.isEmpty());

    }


    @Test
    void willThrownListOfPatientEmpty(){
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> patientService.getAllPatients())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("The list of patients is empty");
        verify(patientRepository , never()).save(any());

    }



}