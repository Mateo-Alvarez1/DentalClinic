package DentalClinic.dentalclinic.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY ="cae6b05b4f64d89b0648522349dd109a209cc0e875060500f8dce10a09e376db";


    // EXTRACT_USERNAME
    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject);
    }

    //VALIDATE_KEY
    public boolean isValidToken(String token ,UserDetails userDetails){
        String username = extractUsername(token);
        return  (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }


    // EXTRACT_CLAIMS
    public <T> T extractClaim(String token , Function<Claims , T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //GENERATE_TOKEN

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }


    public String generateToken(
            Map<String , Object> extraClaims ,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact();
    }


    // GET_KEY
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
