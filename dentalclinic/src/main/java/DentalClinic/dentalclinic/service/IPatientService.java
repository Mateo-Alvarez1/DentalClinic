package DentalClinic.dentalclinic.service;

import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.PatientDto;
import DentalClinic.dentalclinic.repository.entities.Patient;

import java.util.Collection;

public interface IPatientService {
    Patient createPatient(PatientDto patientDto) throws ObjectAlreadyExistException;
    Patient updatePatient(PatientDto patientDto, Long id) throws ObjectAlreadyExistException, ResourceNotFoundException;
    void deletePatient(Long id) throws ObjectAlreadyExistException, ResourceNotFoundException;
    Patient getPatient(Long id) throws ResourceNotFoundException;
    Collection<PatientDto> getAllPatients() throws ResourceNotFoundException;
}
