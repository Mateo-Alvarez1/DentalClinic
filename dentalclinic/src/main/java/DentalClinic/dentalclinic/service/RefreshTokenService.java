package DentalClinic.dentalclinic.service;

import DentalClinic.dentalclinic.repository.RefreshTokenRepository;
import DentalClinic.dentalclinic.repository.UserRepository;
import DentalClinic.dentalclinic.repository.entities.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken =  RefreshToken.builder()
                .user(userRepository.findByEmail(username).get())
                .token(UUID.randomUUID().toString())
                .expirateDate(Instant.now().plusMillis(600000))
                .build();
        return refreshRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if (token.getExpirateDate().compareTo(Instant.now()) < 0){
            refreshRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token was expired");
        }
        return token;
    }

    public String resolveRefreshToken(HttpServletRequest request){
        String refreshTokenHeader = request.getHeader("Refresh-Token");

        if (StringUtils.hasText(refreshTokenHeader)) {
            return refreshTokenHeader;
        }

        return null;
    }

}
