package DentalClinic.dentalclinic.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id" , referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "dentist_id" , referencedColumnName = "id")
    private Dentist dentist;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;


}
