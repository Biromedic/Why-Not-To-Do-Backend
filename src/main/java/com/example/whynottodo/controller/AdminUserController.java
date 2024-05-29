package com.example.whynottodo.controller;

import com.example.whynottodo.Auth.AuthenticationResponse;
import com.example.whynottodo.DTO.UserDTO;
import com.example.whynottodo.DTO.UserRequestDTO;
import com.example.whynottodo.service.AuthenticationServiceImplementation;
import com.example.whynottodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;
    private final AuthenticationServiceImplementation authenticationServiceImplementation;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/registeradmin")
    public ResponseEntity<AuthenticationResponse> createAdmin(@RequestBody UserRequestDTO user) {
        return ResponseEntity.ok(authenticationServiceImplementation.registerAdmin(user));
    }
}
