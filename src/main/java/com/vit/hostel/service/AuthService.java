package com.vit.hostel.service;

import com.vit.hostel.dto.request.LoginRequest;
import com.vit.hostel.dto.request.RegisterRequest;
import com.vit.hostel.dto.response.AuthResponse;
import com.vit.hostel.entity.Hostel;
import com.vit.hostel.entity.User;
import com.vit.hostel.entity.enums.Role;
import com.vit.hostel.exception.BadRequestException;
import com.vit.hostel.exception.ResourceNotFoundException;
import com.vit.hostel.repository.HostelRepository;
import com.vit.hostel.repository.UserRepository;
import com.vit.hostel.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final HostelRepository hostelRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Validate email domain
        if (!request.getEmail().endsWith("@vitstudent.ac.in")) {
            throw new BadRequestException("Email must end with @vitstudent.ac.in");
        }

        // Find hostel
        Hostel hostel = hostelRepository.findByCode(request.getHostelCode())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Hostel not found with code: " + request.getHostelCode()));

        // Create user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .hostel(hostel)
                .roomNumber(request.getRoomNumber())
                .build();

        userRepository.save(user);

        // Generate token
        String token = jwtTokenProvider.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .hostelCode(hostel.getCode())
                .hostelName(hostel.getName())
                .roomNumber(user.getRoomNumber())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase();

        // Check if it's an admin login
        boolean isAdminEmail = email.endsWith("@vitadmin.ac.in");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        // Validate password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword()));

        // Generate token
        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .hostelCode(user.getHostel().getCode())
                .hostelName(user.getHostel().getName())
                .roomNumber(user.getRoomNumber())
                .build();
    }

    public AuthResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return AuthResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .hostelCode(user.getHostel().getCode())
                .hostelName(user.getHostel().getName())
                .roomNumber(user.getRoomNumber())
                .build();
    }
}
