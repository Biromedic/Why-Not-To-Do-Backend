package com.example.whynottodo.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
