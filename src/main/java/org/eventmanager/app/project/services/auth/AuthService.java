package org.eventmanager.app.project.services.auth;

import jakarta.validation.Valid;
import org.eventmanager.app.project.payload.request.auth.SignInRequestPayload;
import org.eventmanager.app.project.payload.request.auth.SignUpRequestPayload;
import org.eventmanager.app.project.payload.response.auth.SignInResponsePayload;
import org.eventmanager.app.project.payload.response.auth.UserInfoPayload;
import org.eventmanager.app.project.security.services.UserDetailsImpl;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    void signUp(SignUpRequestPayload payload);
    SignInResponsePayload signIn(@Valid SignInRequestPayload payload);
    UserInfoPayload getUserInfoByUserDetails(UserDetailsImpl userDetails);
    ResponseCookie getCleanJwtCookie();
}
