package org.eventmanager.app.project.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIExceptionResponse {
    private String message;
    private Integer statusCode;
}
