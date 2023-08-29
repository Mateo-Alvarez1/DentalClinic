package DentalClinic.dentalclinic.service;

import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.model.*;
import DentalClinic.dentalclinic.repository.DentistRepository;
import DentalClinic.dentalclinic.repository.PatientRepository;
import DentalClinic.dentalclinic.repository.UserRepository;
import DentalClinic.dentalclinic.repository.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final DentistService dentistService;
    private final PasswordEncoder passwordEncoder;
    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final ObjectMapper mapper;

    public String registerUser(RegisterRequest registerRequest) throws ObjectAlreadyExistException {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            System.out.println(registerRequest.getEmail());
            throw new ObjectAlreadyExistException("A user is already registered with that email.");
        }
        Address address = registerRequest.getAddress();
        Patient patient = new Patient(registerRequest.getFirstname() , registerRequest.getLastname() , registerRequest.getEmail(), address);
        patientService.createPatient(mapper.convertValue(patient , PatientDto.class));


        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        return "User successfully register";
    }


    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<Patient> patient = null;
        if (user.getRole() == Role.ROLE_USER){
            patient = patientRepository.findByEmail(user.getEmail());
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        return AuthenticationResponse.builder()
                .patient(patient.get())
                .accessToken(refreshToken.getToken())
                .token(jwtService.generateToken(user))
                .role(user.getRole())
                .build();
    }

    public String registerAdmin(RegisterRequest registerRequest) throws ObjectAlreadyExistException {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            System.out.println(registerRequest.getEmail());
            throw new ObjectAlreadyExistException("A user is already registered with that email.");
        }

        Dentist dentist = new Dentist(registerRequest.getFirstname() , registerRequest.getLastname() , registerRequest.getEmail(), registerRequest.getLicense());
        dentistService.createDentist(mapper.convertValue(dentist , DentistDto.class));

        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build();
        userRepository.save(user);
        return "User admin successfully register";
    }


    public AuthenticationResponse authenticateAdmin(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User admin not found"));
        Optional<Dentist> dentist = null;
        if (user.getRole() == Role.ROLE_ADMIN){
            dentist = dentistRepository.findByEmail(user.getEmail());
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        return AuthenticationResponse.builder()
                .dentist(dentist.get())
                .accessToken(refreshToken.getToken())
                .token(jwtService.generateToken(user))
                .role(user.getRole())
                .build();
    }


}
