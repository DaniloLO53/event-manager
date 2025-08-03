package org.eventmanager.app.project.repositories;

import org.eventmanager.app.project.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query(
        value = "SELECT EXISTS (" +
                "  SELECT 1 FROM events e " +
                "  WHERE e.room_id = :roomId " +
                "    AND e.start_time < :endTime " +
                "    AND (:startTime < e.start_time + e.duration_minutes * INTERVAL '1 minute')" +
                ")",
        nativeQuery = true
    )
    boolean existsOverlappingEventInRoom(
        @Param("roomId") UUID roomId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );

    @Query(
        value = "SELECT EXISTS (" +
                "  SELECT 1 FROM events e " +
                "  WHERE e.room_id = :roomId " +
                "    AND e.id <> :eventId " + // <-- A CONDIÇÃO CHAVE
                "    AND e.start_time < :endTime " +
                "    AND (:startTime < e.start_time + e.duration_minutes * INTERVAL '1 minute')" +
                ")",
        nativeQuery = true
    )
    boolean existsOverlappingEventInRoomExcludingId(
        @Param("roomId") UUID roomId,
        @Param("eventId") UUID eventId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );

    @Query(
        nativeQuery = true,
        value = "SELECT EXISTS (" +
                "  SELECT 1 FROM events e WHERE e.id = :eventId AND e.creator_user_id = :userId " +
                "  UNION ALL " +
                "  SELECT 1 FROM event_organizers eo WHERE eo.event_id = :eventId AND eo.user_id = :userId" +
                ")"
    )
    boolean isUserCreatorOrOrganizer(@Param("eventId") UUID eventId, @Param("userId") UUID userId);
}
