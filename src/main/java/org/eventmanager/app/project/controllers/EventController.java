package org.eventmanager.app.project.controllers;

import jakarta.validation.Valid;
import org.eventmanager.app.project.payload.request.events.CreateEventRequestPayload;
import org.eventmanager.app.project.payload.response.events.CreateEventResponsePayload;
import org.eventmanager.app.project.security.services.UserDetailsImpl;
import org.eventmanager.app.project.services.events.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/events")
    public ResponseEntity<CreateEventResponsePayload> createEvent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid CreateEventRequestPayload payload) {
        UUID userId = userDetails.getId();

        CreateEventResponsePayload dto = eventService.createEvent(userId, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<CreateEventResponsePayload> updateEvent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID eventId,
            @RequestBody @Valid CreateEventRequestPayload payload) {
        UUID userId = userDetails.getId();

        CreateEventResponsePayload dto = eventService.updateEvent(userId, eventId, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
