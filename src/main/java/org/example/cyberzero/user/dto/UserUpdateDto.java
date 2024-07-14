package org.example.cyberzero.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String email;
}
