package com.example.whynottodo.DTO;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDTO {
    private String newPassword;
}
