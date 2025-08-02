package org.eventmanager.app.project.services.events;

import jakarta.validation.Valid;
import org.eventmanager.app.project.payload.request.events.CreateEventRequestPayload;
import org.eventmanager.app.project.payload.response.events.CreateEventResponsePayload;

import java.util.UUID;

public interface EventService {
    CreateEventResponsePayload createEvent(UUID userId, @Valid CreateEventRequestPayload payload);
}
