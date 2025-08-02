package org.eventmanager.app.project.payload.response.auth;

import java.util.UUID;

public record UserInfoPayload (
    UUID id,
    String email,
    String jwtCookieString
){}
