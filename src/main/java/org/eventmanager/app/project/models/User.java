package org.eventmanager.app.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.UUID) // Hint for Hibernate to use native UUID type from database, if available
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @NotBlank
    @Size(
            min = 3,
            max = 50,
            message = "Field first_name must have between 3 and 50 characters"
    )
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(
            min = 3,
            max = 50,
            message = "Field last_name must have between 3 and 50 characters"
    )
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @NotBlank
    @Email
    @Size(
            min = 3,
            max = 100,
            message = "Field email must have between 3 and 100 characters"
    )
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @NotBlank
    @Size(
            min = 3,
            max = 255,
            message = "Field password must have between 3 and 255 characters"
    )
    @Column(name = "password", nullable = false)
    private String password;
}