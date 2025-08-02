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
}
