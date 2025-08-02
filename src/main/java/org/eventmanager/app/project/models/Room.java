package org.eventmanager.app.project.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.UUID) // Hint for Hibernate to use native UUID type from database, if available
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private RoomStatus status;

    @Column(name = "area_sqm", precision = 8, scale = 2)
    private BigDecimal areaSqm;

    @Column(name = "has_projector", nullable = false)
    private Boolean hasProjector = false;

    @Column(name = "has_sound_system", nullable = false)
    private Boolean hasSoundSystem = false;

    @Column(name = "has_whiteboard", nullable = false)
    private Boolean hasWhiteboard = false;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> events;
}
