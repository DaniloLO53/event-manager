package org.eventmanager.app.project.security.services;

import org.eventmanager.app.project.exceptions.ResourceNotFoundException;
import org.eventmanager.app.project.models.User;
import org.eventmanager.app.project.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findFirstByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", email));

        return new UserDetailsImpl(email, user.getPassword());
    }
}
