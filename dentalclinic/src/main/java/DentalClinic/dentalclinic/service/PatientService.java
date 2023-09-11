package DentalClinic.dentalclinic.service;

import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.PatientDto;
import DentalClinic.dentalclinic.repository.PatientRepository;
import DentalClinic.dentalclinic.repository.entities.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService implements IPatientService{

    private final PatientRepository patientRepository;
    private final ObjectMapper mapper;

    @Override
    public Patient createPatient(PatientDto patientDto) throws ObjectAlreadyExistException {

        if (patientRepository.existsByEmail(patientDto.getEmail())) {
            log.error("Patient with email " + patientDto.getEmail() + " already exists in the database");
            throw new ObjectAlreadyExistException("Patient already exists in the database");
        }


        Patient patient = new Patient();
        patient.setFirstname(patientDto.getFirstname());
        patient.setSurname(patientDto.getSurname());
        patient.setEmail(patientDto.getEmail());
        patient.setAddress(patientDto.getAddress());

        log.info("Patient successfully persist");
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(PatientDto patientDto, Long id) throws ResourceNotFoundException {

        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (!optionalPatient.isPresent()){
            log.error("Patient with id " + id + " not found in the database");
            throw new ResourceNotFoundException("Patient not found in the database");
        }

        Patient patient = optionalPatient.get();
        patient.setFirstname(patientDto.getFirstname());
        patient.setSurname(patientDto.getSurname());
        patient.setEmail(patientDto.getEmail());
        patient.setAddress(patientDto.getAddress());

       log.info("Patient successfully update");
       return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long id) throws ResourceNotFoundException {

        Boolean optionalPatient = patientRepository.existsById(id);
        if (!optionalPatient){
            log.error("Patient with id " + id + " not found in the database");
            throw new ResourceNotFoundException("Patient not found in the database");
        }
        log.info("Patient successfully delete");
        patientRepository.deleteById(id);
    }

    @Override
    public Patient getPatient(Long id) throws ResourceNotFoundException {

        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if ( !optionalPatient.isPresent() ){
            log.error("Patient with id " + id + " not found in the database");
            throw new ResourceNotFoundException("Patient not found in the database");
        }

        log.info("Patient successfully found");
        return optionalPatient.get();
    }

    @Override
    public Collection<PatientDto> getAllPatients() throws ResourceNotFoundException {
       List<Patient> patients = patientRepository.findAll();
        Set<PatientDto> patientDtos = new HashSet<>();
        for ( Patient patient : patients ) {
            log.info("Patients successfully listed");
            patientDtos.add(mapper.convertValue(patient , PatientDto.class));
        }
        if (patients.isEmpty()){
            log.error("Empty list of patients");
            throw new ResourceNotFoundException("The list of patients is empty");
        }
        return patientDtos;
    }
}