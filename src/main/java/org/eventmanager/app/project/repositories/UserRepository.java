package org.eventmanager.app.project.repositories;

import org.eventmanager.app.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<UserDetails> findFirstByEmail(String email);
}
