package org.eventmanager.app.project.payload.response.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInResponsePayload {
    @NotBlank(message = "O email não pode estar em branco.")
    private String email;

    @NotNull(message = "O jwt não pode ser nulo.")
    private String jwtCookieString;
}
