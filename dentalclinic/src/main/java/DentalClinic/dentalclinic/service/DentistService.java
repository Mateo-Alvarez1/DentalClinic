package DentalClinic.dentalclinic.service;
import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.DentistDto;
import DentalClinic.dentalclinic.repository.DentistRepository;
import DentalClinic.dentalclinic.repository.entities.Dentist;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DentistService implements IDentistService{


    private final DentistRepository dentistRepository;
    private final ObjectMapper mapper;

    @Override
    public Dentist createDentist(DentistDto dentistDto) throws ObjectAlreadyExistException {

        if (dentistRepository.existsByEmail(dentistDto.getEmail())) {
            log.error("Dentist with email " + dentistDto.getEmail() + " already exists in the database");
            throw new ObjectAlreadyExistException("Dentist already exists in the database");
        }

        Dentist dentist = new Dentist();
        dentist.setFirstname(dentistDto.getFirstname());
        dentist.setSurname(dentistDto.getSurname());
        dentist.setEmail(dentistDto.getEmail());
        dentist.setLicense(dentistDto.getLicense());

        log.info("Dentist successfully persist");
        return dentistRepository.save(dentist);
    }

    @Override
    public Dentist updateDentist(DentistDto dentistDto, Long id) throws ResourceNotFoundException {

        Optional<Dentist> optionalDentist = dentistRepository.findById(id);
        if (!optionalDentist.isPresent()){
            log.error("Dentist with id " + id + " not found in the database");
            throw new ResourceNotFoundException("Dentist not found in the database");
        }

        Dentist dentist = optionalDentist.get();
        dentist.setFirstname(dentistDto.getFirstname());
        dentist.setSurname(dentistDto.getSurname());
        dentist.setEmail(dentistDto.getEmail());
        dentist.setLicense(dentistDto.getLicense());

        log.info("Dentist successfully update");
        return dentistRepository.save(dentist);
    }

    @Override
    public void deleteDentist(Long id) throws ResourceNotFoundException {

        Boolean optionalDentist = dentistRepository.existsById(id);
        if (!optionalDentist){
            log.error("Dentist with id " + id + " not found in the database");
            throw new ResourceNotFoundException("Dentist not found in the database");
        }
        log.info("Dentist successfully delete");
        dentistRepository.deleteById(id);
    }

    @Override
    public Dentist getDentist(Long id) throws ResourceNotFoundException {

        Optional<Dentist> optionalDentist = dentistRepository.findById(id);
        if ( !optionalDentist.isPresent() ){
            log.error("Dentist with id " + id + " not found in the database");
            throw new ResourceNotFoundException("Dentist not found in the database");
        }

        log.info("Dentist successfully found");
        return optionalDentist.get();
    }

    @Override
    public Dentist getDentistByFirstnameAndSurname(String firstname, String surname) throws ResourceNotFoundException {
        Optional<Dentist> optionalDentist = dentistRepository.findByFirstnameAndSurname(firstname , surname);
        if (!optionalDentist.isPresent()) {
            log.error("Dentist with name " + firstname + surname + " not found in the database");
            throw new ResourceNotFoundException("Dentist not found in the database");
        }
        log.info("Dentist successfully found");
        return optionalDentist.get();
    }

    @Override
    public Collection<DentistDto> getAllDentists() throws ResourceNotFoundException {
        Collection<Dentist> dentists = dentistRepository.findAll();
        Set<DentistDto> dentistDtos = new HashSet<>();
        for ( Dentist dentist : dentists ) {
            log.info("Dentists successfully listed");
            dentistDtos.add(mapper.convertValue(dentist , DentistDto.class));
        }
        if (dentists.isEmpty()){
            log.error("Empty list of dentists");
            throw new ResourceNotFoundException("The list of dentists is empty");
        }
        return dentistDtos;
    }
}
