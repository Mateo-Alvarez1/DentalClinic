package DentalClinic.dentalclinic.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street ;

    @Column(name = "locality")
    private String locality;

    @Column(name = "province")
    private String province;

    @Column(name = "number")
    private Integer number;

    public Address(String street, String locality, String province, Integer number) {
        this.street = street;
        this.locality = locality;
        this.province = province;
        this.number = number;
    }
}
