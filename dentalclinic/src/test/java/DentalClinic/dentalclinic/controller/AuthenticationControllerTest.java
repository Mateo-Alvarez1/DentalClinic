package DentalClinic.dentalclinic.controller;
import DentalClinic.dentalclinic.model.AuthenticationRequest;
import DentalClinic.dentalclinic.model.AuthenticationResponse;
import DentalClinic.dentalclinic.model.RegisterRequest;
import DentalClinic.dentalclinic.repository.UserRepository;
import DentalClinic.dentalclinic.repository.entities.Address;
import DentalClinic.dentalclinic.repository.entities.Role;
import DentalClinic.dentalclinic.repository.entities.User;
import DentalClinic.dentalclinic.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationService autheticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void registerAndCreateUser() throws Exception {

        var firstname = "test";
        var surname = "tested";
        var email = "test@test.com";
        var password = "123";
        var license = "456";
        var address = new Address("test" , "tested" , "testing", 123);

        var request = new RegisterRequest(
                firstname,
                surname,
                email,
                password,
                license,
                address
        );


        when(userRepository.existsByEmail(email)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .post("/register")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(status().isConflict()
            );

        verify(autheticationService , never()).registerUser(any(RegisterRequest.class));

        reset(userRepository);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(autheticationService.registerUser(request)).thenReturn("User successfully register");


        mvc.perform(MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk()
        );

        verify(autheticationService , times(1)).registerUser(request);

    }

    @Test
    public String testAuthenticateUser() throws Exception {

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");


        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AuthenticationResponse authenticationResponse = mapper.readValue(responseContent , AuthenticationResponse.class);
        // leer el contenido de la respuesta HTTP en un objeto de la clase AuthenticationResponse
        return authenticationResponse.getToken() ;

    }


    @Test
    void testRegisterAndCreateAdmin() throws Exception {

        var firstname = "test";
        var surname = "tested";
        var email = "test@test.com";
        var password = "123";
        var license = "456";
        var address = new Address("test" , "tested" , "testing", 123);

        var request = new RegisterRequest(
                firstname,
                surname,
                email,
                password,
                license
        );

        when(userRepository.existsByEmail(email)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .post("/registerAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(autheticationService , never()).registerAdmin(any(RegisterRequest.class));

        reset(userRepository);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(autheticationService.registerUser(request)).thenReturn("User admin successfully register");

        mvc.perform(MockMvcRequestBuilders
                .post("/authenticate")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(autheticationService , times(1)).registerAdmin(request);

    }

    @Test
    String testAuthenticateAdmin() throws Exception {
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);

        AuthenticationRequest authenticationRequest  = new AuthenticationRequest();
        authenticationRequest.setEmail("test@email.com");
        authenticationRequest.setPassword("password");

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/autheticateAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AuthenticationResponse response = mapper.readValue(responseContent, AuthenticationResponse.class);
        return response.getToken();

    }



}