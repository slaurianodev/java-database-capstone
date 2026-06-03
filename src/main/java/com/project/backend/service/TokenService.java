package com.project.backend.service;

/**
 * Created by Sergio.
 */
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    /**
     * Validates a token against a required role.
     *
     * @param token the token received from the request
     * @param role  the expected role (e.g., "admin", "doctor")
     * @return an empty map if valid, otherwise a map with error details
     */
    public Map<String, Object> validateToken(String token, String role) {
        Map<String, Object> result = new HashMap<>();

        // Basic validation (placeholder logic)
        if (token == null || token.isEmpty()) {
            result.put("error", "Token is missing");
            return result;
        }

        // Simulated token validation logic
        // Example rule: token must start with role name (for demo purposes)
        if (!token.startsWith(role)) {
            result.put("error", "Invalid token for role: " + role);
            return result;
        }

        return result;
    }
}
