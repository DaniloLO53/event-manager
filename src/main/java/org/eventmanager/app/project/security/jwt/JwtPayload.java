package org.eventmanager.app.project.security.jwt;

import jakarta.validation.Payload;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class JwtPayload {
    private @Getter UUID id;
    private @Getter String email;

    public JwtPayload(UUID id, String email) {
        this.id = id;
        this.email = email;
    }

    public static class Builder {
        private UUID idField;
        private String emailField;

        public Builder id(UUID id) {
            this.idField = id;
            return this;
        }

        public Builder email(String email) {
            this.emailField = email;
            return this;
        }

        public JwtPayload build() {
            return new JwtPayload(idField, emailField);
        }
    }
}
