package DentalClinic.dentalclinic.service;

import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.DentistDto;
import DentalClinic.dentalclinic.repository.entities.Dentist;

import java.util.Collection;

public interface IDentistService {
    Dentist createDentist(DentistDto dentistDto) throws ObjectAlreadyExistException;
    Dentist updateDentist(DentistDto dentistDto, Long id) throws ResourceNotFoundException;
    void deleteDentist(Long id) throws ResourceNotFoundException;
    Dentist getDentist(Long id) throws ResourceNotFoundException;
    Dentist getDentistByFirstnameAndSurname(String firstname , String surname) throws ResourceNotFoundException;
    Collection<DentistDto> getAllDentists() throws ResourceNotFoundException;
}
