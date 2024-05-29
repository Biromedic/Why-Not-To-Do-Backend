package com.example.whynottodo.service;


import com.example.whynottodo.DTO.ChangePasswordRequestDTO;
import com.example.whynottodo.DTO.PasswordResetDTO;
import com.example.whynottodo.DTO.UserDTO;
import com.example.whynottodo.model.User;
import com.example.whynottodo.model.enums.Role;
import com.example.whynottodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService   {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    private final PasswordEncoder passwordEncoder;

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new NoSuchElementException("There is no user.");
        }

        return mapper.map(user, UserDTO.class);
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()){
            throw new ResourceNotFoundException("User", "id", userId);
        }

        User existingUser = optionalUser.get();

        existingUser.setName(userDTO.getName());
        existingUser.setSurname(userDTO.getSurname());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());

        User updatedUser = userRepository.save(existingUser);

        return mapper.map(updatedUser, UserDTO.class);
    }

    public List<UserDTO> getAllUser() {
        List<User> userList =  userRepository.findAll();
        if (userList.isEmpty()){
            throw new NoSuchElementException("There is no user.");
        }
        return userList.stream().map(user -> mapper.map(user, UserDTO.class)).toList();
    }


    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user","id",userId));
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(usernameOrEmail);
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(usernameOrEmail);
        }
        return optionalUser.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
            super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        }
    }

    public UserDTO getUserDetails(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return mapper.map(user, UserDTO.class);
    }

    public void resetPassword(Authentication authentication, PasswordResetDTO passwordResetDTO) {
        User user = (User) authentication.getPrincipal();
        user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
        userRepository.save(user);
    }

    public void changePassword(String username, ChangePasswordRequestDTO changePasswordRequest) {
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public void assignModeratorRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user","id",userId));
        user.setRole(Role.MODERATOR);
        userRepository.save(user);
    }

}
