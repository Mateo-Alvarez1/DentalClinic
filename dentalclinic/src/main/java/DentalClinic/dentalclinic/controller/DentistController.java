package DentalClinic.dentalclinic.controller;
import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.exceptions.ResourceNotFoundException;
import DentalClinic.dentalclinic.model.AppointmentDto;
import DentalClinic.dentalclinic.model.DentistDto;
import DentalClinic.dentalclinic.repository.entities.Dentist;
import DentalClinic.dentalclinic.service.AppointmentService;
import DentalClinic.dentalclinic.service.DentistService;
import DentalClinic.dentalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/dentists") //This is a good way of creating your url
public class DentistController {

    private final PatientService patientService;
    private final DentistService dentistService;
    private final AppointmentService appointmentService;

    //DENTIST
    /* @PostMapping("/create") you shouldn't use actions to describe your intention, use http verbs instead,
    rest works according http states and verbs
    */
    @PostMapping() //here your using http post to persist a resource
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createDentist(@RequestBody DentistDto dentistDto) throws ObjectAlreadyExistException {
        dentistService.createDentist(dentistDto);
        return ResponseEntity.ok("Dentist " + dentistDto.getFirstname() + " successfully persist");
    }

    //@PutMapping("/update/{id}")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateDentist(@RequestBody DentistDto dentistDto, @PathVariable Long id) throws ResourceNotFoundException {
        dentistService.updateDentist(dentistDto , id);
        return ResponseEntity.ok().body("Dentist " + id + " successfully updated");
    }

    //@DeleteMapping("/delete/{id}")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteDentist(@PathVariable Long id) throws ResourceNotFoundException {
        dentistService.deleteDentist(id);
        return ResponseEntity.ok().body("Dentist " + id + " successfully deleted");
    }

    //@GetMapping("/getDentist/{id}")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Dentist> getDentist(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(dentistService.getDentist(id));
    }

    //@GetMapping("/getAll")
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN' , 'ROLE_USER')")
    public ResponseEntity<Collection<DentistDto>> getAllDentist() throws ResourceNotFoundException {
        return ResponseEntity.ok().body(dentistService.getAllDentists());
    }


    //APPOINTMENT
    /*
    here you need to get dentist's appointments, so a better way of declaring this route would be something like
    @GetMapping("/appointments")  here your're getting all appointments of a specific dentist
     */
    @GetMapping("/{dentistId}/appointments/getByDentist")
    @PreAuthorize("hasRole('ROLE_ADMIN' , 'ROLE_USER')")
    //I think you should return a implementation of data structure more commonly List not Collection because you're doing it very generic
    public ResponseEntity<Collection<AppointmentDto>> findAppointmentByDentist(@RequestBody DentistDto dentistDto) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(appointmentService.findAppointmentByDentist(dentistDto));
    }


    @GetMapping("/appointments/getAll") //The same as above
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<AppointmentDto>> getAllAppointments() throws ResourceNotFoundException {
        return ResponseEntity.ok().body(appointmentService.getAllAppointments());
    }

    @PutMapping("/appointments/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateAppointment(@RequestBody AppointmentDto appointmentDto , @PathVariable Long id) throws ResourceNotFoundException {
        appointmentService.updateAppointment(appointmentDto , id);
        return ResponseEntity.ok().body("Appointment " + appointmentDto.getAppointmentDate() + " successfully update");
    }

    //PATIENT

    @DeleteMapping("/patients/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) throws ResourceNotFoundException {
        patientService.deletePatient(id);
        return ResponseEntity.ok().body("Patient " + id + " successfully deleted");
    }

    @GetMapping("/patients/getAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getAllPatients() throws ResourceNotFoundException {
        patientService.getAllPatients();
        return ResponseEntity.ok().body("Patients successfully listed");
    }

}
