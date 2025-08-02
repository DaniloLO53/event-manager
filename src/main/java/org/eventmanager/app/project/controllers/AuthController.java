package org.eventmanager.app.project.controllers;

import jakarta.validation.Valid;
import org.eventmanager.app.project.payload.request.auth.SignInRequestPayload;
import org.eventmanager.app.project.payload.request.auth.SignUpRequestPayload;
import org.eventmanager.app.project.payload.response.auth.SignInResponsePayload;
import org.eventmanager.app.project.payload.response.auth.UserInfoPayload;
import org.eventmanager.app.project.security.services.UserDetailsImpl;
import org.eventmanager.app.project.services.auth.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoPayload> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserInfoPayload payload = authService.getUserInfoByUserDetails(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(payload);
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
                .header(HttpHeaders.SET_COOKIE, response.jwtCookieString())
                .body(response);
    }

    @PatchMapping("/signout")
    public ResponseEntity<?> signOut() {
        ResponseCookie cleanJwtCookie = authService.getCleanJwtCookie();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString())
                .body(null);
    }
}
