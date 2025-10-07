package com.example.StudySync.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Autowired
    UserDetailsService userDetailsService;


    // A predefined secret key (Base64 encoded string) used for signing JWT tokens
    private static final String SECRET = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg=\r\n";

    // A dynamically generated secret key for signing JWT tokens
    private String secreteKey;

    // Access token expiration time (e.g., 3 minutes)
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 3;
    // Refresh token expiration time (e.g., 24 hours)
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;

    // Constructor: Generates a new secret key when the service is initialized
    public JwtUtil() {
        secreteKey = generateSecretKey();
    }

    /**
     * Generates a random secret key using HmacSHA256 algorithm.
     * The key is encoded in Base64 format for easier storage and retrieval.
     */
    public String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256"); // Create key generator for HmacSHA256
            SecretKey secretKey = keyGen.generateKey(); // Generate a secret key
            System.out.println("Secret Key : " + secretKey.toString()); // Print the generated key
            return Base64.getEncoder().encodeToString(secretKey.getEncoded()); // Convert to Base64 string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e); // Handle error if the algorithm is not found
        }
    }

    public String generateAccessToken(String username) {
        return generateToken(username, ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * Generates a JWT refresh token for a given username.
     * @param username The username for which the token is generated.
     * @return A signed JWT refresh token as a String.
     */
    public String generateRefreshToken(String username) {
        return generateToken(username, REFRESH_TOKEN_EXPIRATION);
    }

    /**
     * Generates a JWT token for a given username.
     * @param email The username for which the token is generated.
     * @return A signed JWT token as a String.
     */
    public String generateToken(String email, long expiration) {
        Map<String, Object> claims = new HashMap<>(); // Stores user-specific claims (currently empty)
        return Jwts.builder()
                .setClaims(claims) // Attach claims (metadata, roles, etc.) - empty in this case
                .setSubject(email) // Set the username as the subject of the token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set token creation timestamp
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Set expiration (3 minutes)
                .signWith(getKey(), SignatureAlgorithm.HS256) // Sign the token using HmacSHA256
                .compact();// Build the token and return it as a String
    }

    /**
     * Converts the secret key from Base64 string to a Key object required for signing JWTs.
     * @return A Key object used for signing and verifying JWT tokens.
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secreteKey); // Decode the Base64 string into bytes
        return Keys.hmacShaKeyFor(keyBytes); // Convert the bytes into an HmacSHA key
    }

    public String extractEmail(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractEmail(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Authentication getAuthentication(String token) {
        String email = extractEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }


}
