package org.eventmanager.app.project.services.auth;

import org.eventmanager.app.project.payload.request.SignUpRequestPayload;

public interface AuthService {
    void signUp(SignUpRequestPayload payload);
}
