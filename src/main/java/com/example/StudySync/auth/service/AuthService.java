package com.example.StudySync.auth.service;

import com.example.StudySync.auth.dto.AuthResponse;
import com.example.StudySync.auth.dto.LoginRequest;
import com.example.StudySync.auth.dto.RegisterRequest;
import com.example.StudySync.auth.model.User;
import com.example.StudySync.auth.repository.UserRepository;
import com.example.StudySync.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // register user
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
        String accessToken = jwtUtil.generateAccessToken(registerRequest.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(registerRequest.getEmail());
        return new AuthResponse(accessToken,refreshToken);
    }
    // login user
    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        String accessToken = jwtUtil.generateAccessToken(loginRequest.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getEmail());
        return new AuthResponse(accessToken,refreshToken);
    }

    // refresh token
    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtUtil.extractEmail(refreshToken);
        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(refreshToken, userDetails)) {
                String newAccessToken = jwtUtil.generateAccessToken(username);
                String newRefreshToken = jwtUtil.generateRefreshToken(username);
                return new AuthResponse(newAccessToken, newRefreshToken);
            }
        }
        throw new RuntimeException("Invalid or expired refresh token");
    }



}
