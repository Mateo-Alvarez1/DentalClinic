package DentalClinic.dentalclinic.controller;

import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.AddressDto;
import DentalClinic.dentalclinic.model.AppointmentDto;
import DentalClinic.dentalclinic.model.DentistDto;
import DentalClinic.dentalclinic.model.PatientDto;
import DentalClinic.dentalclinic.repository.entities.Address;
import DentalClinic.dentalclinic.repository.entities.Dentist;
import DentalClinic.dentalclinic.repository.entities.Patient;
import DentalClinic.dentalclinic.service.AppointmentService;
import DentalClinic.dentalclinic.service.DentistService;
import DentalClinic.dentalclinic.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/patients")
public class PatientController {

    private final DentistService dentistService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    //    PATIENT

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createPatient(@RequestBody PatientDto patientDto, Principal p) throws ObjectAlreadyExistException {
        patientService.createPatient(patientDto);
        return ResponseEntity.ok("Patient " + patientDto.getFirstname() + " successfully persist");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String>updatePatient(@RequestBody PatientDto patientDto , @PathVariable Long id) throws ResourceNotFoundException {
        patientService.updatePatient(patientDto , id);
        return ResponseEntity.ok().body("Patient " + id + " successfully updated");
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @GetMapping() // Arreglar para que me devuelva la lista
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getAllPatients() throws ResourceNotFoundException {
        patientService.getAllPatients();
        return ResponseEntity.ok().body("Patients successfully listed");
    }


    //APPOINTMENT
    @PostMapping("/appointments")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentDto appointmentDto) throws ObjectAlreadyExistException {
        appointmentService.createAppointment(appointmentDto);
        return ResponseEntity.ok().body("Appointment " + appointmentDto.getAppointmentDate() + " successfully persist");
    }

    @GetMapping("/appointments/getByPatient/{patientId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Collection<AppointmentDto>> findAppointmentByPatient(@PathVariable Long patientId , Principal P) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(appointmentService.findAppointmentByPatientId(patientId));
    }

    @DeleteMapping("/appointments/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) throws ResourceNotFoundException {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().body("Appointment " + id + " successfully deleted");
    }

    @GetMapping("/appointments")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Collection<AppointmentDto>> getAllAppointments() throws ResourceNotFoundException {
        return ResponseEntity.ok().body(appointmentService.getAllAppointments());
    }


    //DENTIST

    @GetMapping("/dentists/getDentistByFullName/{firstname}/{surname}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Dentist> getDentistByFullName(@PathVariable String firstname , @PathVariable String surname ) throws ResourceNotFoundException {
        return ResponseEntity.ok(dentistService.getDentistByFirstnameAndSurname(firstname , surname));
    }
    @GetMapping("/dentist")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Collection<DentistDto>> getAllDentist() throws ResourceNotFoundException {
        return ResponseEntity.ok().body(dentistService.getAllDentists());
    }


}
