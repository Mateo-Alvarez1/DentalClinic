package DentalClinic.dentalclinic.controller;
import DentalClinic.dentalclinic.repository.entities.Address;
import DentalClinic.dentalclinic.repository.entities.Patient;
import DentalClinic.dentalclinic.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(JwtService.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthenticationControllerTest authenticationControllerTest;

    private final ObjectMapper mapper = new ObjectMapper();
    @Test
    void createPatient() throws Exception {
        var firstname = "testName";
        var surname = "testSurname";
        var email =    "test_user@test.com";
        var address = new Address( "test" , "tested" , "testing" , 1234);

        var request = new Patient(
                firstname  ,
                surname  ,
                email ,
                address
        );

        String token = authenticationControllerTest.testAuthenticateUser();


        mvc.perform(MockMvcRequestBuilders
                .post("api/v1/patients/create")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization",  "Bearer " + token))
                .andExpect(status().isCreated()
        );
    }









}