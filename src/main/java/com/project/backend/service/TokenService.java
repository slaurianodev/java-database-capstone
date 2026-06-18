package com.project.backend.service;

/**
 * Created by Sergio.
 */
import com.project.backend.repository.AdminRepository;
import com.project.backend.repository.DoctorRepository;
import com.project.backend.repository.PatientRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenService {

    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 days

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Value("${jwt.secret}")
    private String secret;

    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {

        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Generates a JWT token for the specified user identifier.
     *
     * @param identifier Username (Admin) or Email (Doctor/Patient)
     * @return Generated JWT token
     */
    public String generateToken(String identifier) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(identifier)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the identifier from the JWT token.
     *
     * @param token JWT token
     * @return Username or email stored as the subject
     */
    public String extractIdentifier(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Validates the token against the specified user type.
     *
     * @param token JWT token
     * @param user  User type (admin, doctor, patient)
     * @return true if token is valid and user exists; otherwise false
     */
    public boolean validateToken(String token, String user) {

        try {
            String identifier = extractIdentifier(token);

            switch (user.toLowerCase()) {

                case "admin":
                    return adminRepository.findByUsername(identifier) != null;

                case "doctor":
                    return doctorRepository.findByEmail(identifier) != null;

                case "patient":
                    return patientRepository.findByEmail(identifier) != null;

                default:
                    return false;
            }

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Retrieves the signing key used to sign and verify JWT tokens.
     *
     * @return SecretKey used for JWT signing
     */
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
}
