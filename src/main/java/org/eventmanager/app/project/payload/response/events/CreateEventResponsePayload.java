package org.eventmanager.app.project.payload.response.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventResponsePayload {
    @NotNull
    @NotBlank
    @Size(
            min = 3,
            max = 255,
            message = "Field title must have between 3 and 255 characters"
    )
    private String title;

    private String description;

    @NotNull
    private OffsetDateTime startTime;

    @NotNull
    private Integer durationMinutes;
}
