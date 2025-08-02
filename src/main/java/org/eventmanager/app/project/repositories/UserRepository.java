package org.eventmanager.app.project.repositories;

import org.eventmanager.app.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByEmail(String email);
}
