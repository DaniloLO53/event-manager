package org.eventmanager.app.project.repositories;

import org.eventmanager.app.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByEmail(String email);
    boolean existsByEmail(String email);
    User findFirstById(UUID id);
}
