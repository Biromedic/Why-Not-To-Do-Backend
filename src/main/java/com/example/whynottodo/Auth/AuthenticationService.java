package com.example.whynottodo.Auth;

import com.example.whynottodo.DTO.UserLoginDTO;
import com.example.whynottodo.DTO.UserRequestDTO;

public interface AuthenticationService {
    /**
     * Authenticates a user based on the provided login details and returns an authentication response.
     *
     * @param loginDto The DTO containing the user's login credentials.
     * @return An AuthenticationResponse containing authentication details, such as a JWT token.
     */
    AuthenticationResponse authenticateUser(UserLoginDTO loginDto);

    /**
     * Registers a new user into the system and returns an authentication response.
     *
     * @param request The DTO containing the user's registration details.
     * @return An AuthenticationResponse containing authentication details for the newly registered user.
     */
    AuthenticationResponse registerUser(UserRequestDTO request);
}
