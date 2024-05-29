package com.example.whynottodo.DTO;

import com.example.whynottodo.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private Role role;
}
