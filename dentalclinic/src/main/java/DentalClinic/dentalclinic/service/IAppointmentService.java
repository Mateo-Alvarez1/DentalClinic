package DentalClinic.dentalclinic.service;

import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.AppointmentDto;
import DentalClinic.dentalclinic.model.DentistDto;
import DentalClinic.dentalclinic.model.PatientDto;
import DentalClinic.dentalclinic.repository.entities.Appointment;

import java.util.Collection;

public interface IAppointmentService {
    Appointment createAppointment(AppointmentDto appointmentDto) throws ObjectAlreadyExistException;
    Appointment updateAppointment(AppointmentDto appointmentDto , Long id) throws ResourceNotFoundException;
    void deleteAppointment( Long id) throws ResourceNotFoundException;
    Collection<AppointmentDto> getAllAppointments() throws ResourceNotFoundException;
    Collection<AppointmentDto> findAppointmentByPatientId(Long patientId) throws ResourceNotFoundException;
    Collection<AppointmentDto> findAppointmentByDentistId(Long dentistId) throws ResourceNotFoundException;


}
