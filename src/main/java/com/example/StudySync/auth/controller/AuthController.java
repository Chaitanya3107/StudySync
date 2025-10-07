package com.example.StudySync.auth.controller;
import com.example.StudySync.auth.dto.AuthResponse;
import com.example.StudySync.auth.dto.LoginRequest;
import com.example.StudySync.auth.dto.RegisterRequest;
import com.example.StudySync.auth.security.JwtUtil;
import com.example.StudySync.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    final private JwtUtil jwtUtil;
//    private final UserDetails

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest){
        try {
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Catches exceptions like "Email already exists" from the service layer
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest){
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // Catches authentication failures (e.g., wrong password)
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            // Catches any other unexpected exceptions
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }

    @GetMapping("refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            try {
                AuthResponse newTokenResponse = authService.refreshToken(refreshToken);
                return ResponseEntity.ok(newTokenResponse);
            } catch (RuntimeException e) {
                return ResponseEntity.status(401).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(401).body("Refresh token is missing or malformed");
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> register(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

}
