package ru.romanov.moneytransferservice.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest extends CreateUserRequest {

    @Size(min = 10)
    @NotBlank
    private String password;

    @Size(min = 10)
    @NotBlank
    private String passwordConfirm;
}
