package com.example.whynottodo.controller;


import com.example.whynottodo.Auth.AuthenticationResponse;
import com.example.whynottodo.DTO.*;
import com.example.whynottodo.service.AuthenticationServiceImplementation;
import com.example.whynottodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationServiceImplementation authenticationServiceImplementation;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody UserRequestDTO user) {
        return ResponseEntity.ok(authenticationServiceImplementation.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserLoginDTO loginDto) {

        return ResponseEntity.ok(authenticationServiceImplementation.authenticateUser((loginDto)));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserDetails(Authentication authentication) {
        UserDTO userDTO = userService.getUserDetails(authentication);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(Authentication authentication, @RequestBody PasswordResetDTO passwordResetDTO) {
        userService.resetPassword(authentication, passwordResetDTO);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequest, Authentication authentication) {
        String username = authentication.getName();
        userService.changePassword(username, changePasswordRequest);
    }


}
