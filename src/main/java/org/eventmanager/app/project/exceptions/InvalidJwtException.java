package org.eventmanager.app.project.exceptions;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String message, Exception e) {
        super(message, e);
    }
}
