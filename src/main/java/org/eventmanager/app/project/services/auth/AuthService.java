package org.eventmanager.app.project.services.auth;

import jakarta.validation.Valid;
import org.eventmanager.app.project.payload.request.auth.SignInRequestPayload;
import org.eventmanager.app.project.payload.request.auth.SignUpRequestPayload;
import org.eventmanager.app.project.payload.response.auth.SignInResponsePayload;

public interface AuthService {
    void signUp(SignUpRequestPayload payload);
    SignInResponsePayload signIn(@Valid SignInRequestPayload payload);
}
