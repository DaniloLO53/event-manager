package org.eventmanager.app.project.controllers;

import jakarta.validation.Valid;
import org.eventmanager.app.project.payload.request.auth.SignInRequestPayload;
import org.eventmanager.app.project.payload.request.auth.SignUpRequestPayload;
import org.eventmanager.app.project.payload.response.auth.SignInResponsePayload;
import org.eventmanager.app.project.services.auth.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequestPayload payload) {
        authService.signUp(payload);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<SignInResponsePayload> signIn(@RequestBody @Valid SignInRequestPayload payload) {
        SignInResponsePayload response = authService.signIn(payload);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, response.getJwtCookieString())
                .body(response);
    }
}
