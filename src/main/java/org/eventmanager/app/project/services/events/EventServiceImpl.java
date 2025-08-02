package org.eventmanager.app.project.services.events;

import org.eventmanager.app.project.exceptions.ConflictException;
import org.eventmanager.app.project.exceptions.ResourceNotFoundException;
import org.eventmanager.app.project.models.Event;
import org.eventmanager.app.project.models.Room;
import org.eventmanager.app.project.models.RoomStatus;
import org.eventmanager.app.project.models.User;
import org.eventmanager.app.project.payload.request.events.CreateEventRequestPayload;
import org.eventmanager.app.project.payload.response.events.CreateEventResponsePayload;
import org.eventmanager.app.project.repositories.EventRepository;
import org.eventmanager.app.project.repositories.RoomRepository;
import org.eventmanager.app.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, RoomRepository roomRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CreateEventResponsePayload createEvent(UUID userId, CreateEventRequestPayload payload) {
        User author = userRepository.findFirstById(userId);

        UUID roomId = payload.getRoomId();
        Room room = roomRepository
                .findFirstById(roomId).orElseThrow(() -> new ResourceNotFoundException("Sala", "id", roomId.toString()));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new ConflictException("A sala '" + room.getName() + "' não está disponível para agendamentos.");
        }

        Instant startTime = payload.getStartTime().toInstant();
        Instant endTime = startTime.plus(payload.getDurationMinutes(), ChronoUnit.MINUTES);
        if (eventRepository.existsOverlappingEventInRoom(roomId, startTime, endTime)) {
            throw new ConflictException("Já existe um evento marcado neste horário para a sala selecionada.");
        }

        Event event = modelMapper.map(payload, Event.class);
        // Por algum motivo o modelMapper nao esta convertendo o startTime
        if (payload.getStartTime() != null) {
            event.setStartTime(payload.getStartTime().toInstant());
        }
        event.setCreator(author);
        event.setRoom(room);

        Event savedEvent = eventRepository.save(event);

        CreateEventResponsePayload savedPayload = modelMapper.map(savedEvent, CreateEventResponsePayload.class);
        savedPayload.setStartTime(payload.getStartTime());

        return savedPayload;
    }
}
