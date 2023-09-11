package DentalClinic.dentalclinic.controller;

import DentalClinic.dentalclinic.exceptions.ObjectAlreadyExistException;
import DentalClinic.dentalclinic.model.AuthenticationRequest;
import DentalClinic.dentalclinic.model.AuthenticationResponse;
import DentalClinic.dentalclinic.model.RefreshTokenRequest;
import DentalClinic.dentalclinic.model.RegisterRequest;
import DentalClinic.dentalclinic.repository.entities.RefreshToken;
import DentalClinic.dentalclinic.repository.entities.Role;
import DentalClinic.dentalclinic.service.AuthenticationService;
import DentalClinic.dentalclinic.service.JwtService;
import DentalClinic.dentalclinic.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?>registerAndCreateUser(@RequestBody RegisterRequest registerRequest) throws ObjectAlreadyExistException {
        return ResponseEntity.ok().body(authenticationService.registerUser( registerRequest ));
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?>registerAndCreateAdmin(@RequestBody RegisterRequest registerRequest) throws ObjectAlreadyExistException {
        return ResponseEntity.ok().body(authenticationService.registerAdmin( registerRequest ));
    }

    @PostMapping("/autheticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok().body(authenticationService.authenticateUser(authenticationRequest));
    }

    @PostMapping("/autheticateAdmin")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok().body(authenticationService.authenticateAdmin(authenticationRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return ResponseEntity.ok(AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .role(Role.valueOf(refreshTokenRequest.getRole()))
                            .build());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found in database"));
    }

}
