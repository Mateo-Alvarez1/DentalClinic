package DentalClinic.dentalclinic.service;
import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.AppointmentDto;
import DentalClinic.dentalclinic.model.DentistDto;
import DentalClinic.dentalclinic.repository.AppointmentRepository;
import DentalClinic.dentalclinic.repository.entities.Appointment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService implements IAppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final ObjectMapper mapper;

    @Override
    public Appointment createAppointment(AppointmentDto appointmentDto) throws ObjectAlreadyExistException {

        if (appointmentRepository.existsByAppointmentDate(appointmentDto.getAppointmentDate())) {
            log.error("Appointment with date " + appointmentDto.getAppointmentDate() + " already exists in the database");
            throw new ObjectAlreadyExistException("Appointment already exists in the database");
        }

        Appointment appointment = new Appointment();
        appointment.setDentist(appointmentDto.getDentist());
        appointment.setPatient(appointmentDto.getPatient());
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());


        log.info("Appointment successfully persist");
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(AppointmentDto appointmentDto, Long id) throws ResourceNotFoundException {

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (!optionalAppointment.isPresent()){
            log.error("Appointment with " + id + " not found in the database");
            throw new ResourceNotFoundException("Appointment not found in the database");
        }

        Appointment appointment = optionalAppointment.get();
        appointment.setDentist(appointmentDto.getDentist());
        appointment.setPatient(appointmentDto.getPatient());
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());

        log.info("Appointment successfully update");
        return appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointment(Long id) throws ResourceNotFoundException {

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (!optionalAppointment.isPresent()){
            log.error("Appointment with id " + id + " not found in the database");
            throw new ResourceNotFoundException("Appointment not found in the database");
        }
        log.info("Appointment successfully delete");
        appointmentRepository.deleteById(id);
    }


    @Override
    public Collection<AppointmentDto> getAllAppointments() throws ResourceNotFoundException {
        Collection<Appointment> appointments = appointmentRepository.findAll();
        Set<AppointmentDto> appointmentDtos = new HashSet<>();
        for ( Appointment appointment: appointments  ) {
            log.info("Appointments successfully listed");
            appointmentDtos.add(mapper.convertValue( appointment, AppointmentDto.class));
        }
        if (appointments .isEmpty()){
            log.error("Empty list of appointments");
            throw new ResourceNotFoundException("The list of appointments is empty");
        }
        return appointmentDtos;
    }


    @Override
    public Collection<AppointmentDto> findAppointmentByPatientId(Long patientId) throws ResourceNotFoundException {
        Collection<Appointment> appointments = appointmentRepository.findAppointmentByPatientId(patientId);
        Set<AppointmentDto> appointmentDtos = new HashSet<>();
        for ( Appointment appointment : appointments ) {
            log.info("An appointment was found for this patient");
            appointmentDtos.add(mapper.convertValue(appointment  , AppointmentDto.class));
        }
        if(appointments.isEmpty()){
            log.error("No appointment was found for this patient");
            throw new ResourceNotFoundException("No appointment was found for this patient");
        }
        return appointmentDtos;
    }

    @Override
    public Collection<AppointmentDto> findAppointmentByDentistId(Long dentistId) throws ResourceNotFoundException {
        Collection<Appointment> appointments = appointmentRepository.findAppointmentByDentistId(dentistId);
        Set<AppointmentDto> appointmentDtos = new HashSet<>();
        for ( Appointment appointment : appointments ) {
            log.info("An appointment was found for that dentist");
            appointmentDtos.add(mapper.convertValue(appointment  , AppointmentDto.class));
        }
        if(appointments.isEmpty()){
            log.error("No appointment was found for this dentist");
            throw new ResourceNotFoundException("No appointment was found for this dentist");
        }
        return appointmentDtos;
    }

}
