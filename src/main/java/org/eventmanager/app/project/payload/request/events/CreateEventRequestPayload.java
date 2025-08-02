package org.eventmanager.app.project.payload.request.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequestPayload {
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

    @NotNull
    private UUID roomId;
}
