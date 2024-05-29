package com.example.whynottodo.service;

import com.example.whynottodo.Auth.AuthenticationResponse;
import com.example.whynottodo.Auth.AuthenticationService;
import com.example.whynottodo.DTO.UserLoginDTO;
import com.example.whynottodo.DTO.UserRequestDTO;
import com.example.whynottodo.model.User;
import com.example.whynottodo.model.enums.Role;
import com.example.whynottodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public AuthenticationResponse authenticateUser(UserLoginDTO loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            User user = userRepository.findByUsername(loginDto.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwtToken = JwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Incorrect username or password", e);
        }
    }

    @Override
    public AuthenticationResponse registerUser(UserRequestDTO request) {
        System.out.println("Request DTO: " + request);

        var user = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        //System.out.println("Before save - the username is: " + user.getUsername());

        userRepository.save(user);
        //System.out.println("After save - the username is: " + user.getUsername());

        var jwtToken = JwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
    public AuthenticationResponse registerAdmin(UserRequestDTO request) {

        var admin = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
        var jwtToken = JwtService.generateToken(admin);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse registerMod(UserRequestDTO request) {

        var mod = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(Role.MODERATOR)
                .build();
        userRepository.save(mod);
        var jwtToken = JwtService.generateToken(mod);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
