package org.eventmanager.app.project.services.auth;

import org.eventmanager.app.project.exceptions.ConflictException;
import org.eventmanager.app.project.models.User;
import org.eventmanager.app.project.payload.request.SignUpRequestPayload;
import org.eventmanager.app.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void signUp(SignUpRequestPayload payload) {
        String email = payload.getEmail();
        String password = payload.getPassword();
        String passwordConfirmation = payload.getPasswordConfirmation();

        if (userRepository.existsByEmail(email)) {
           throw new ConflictException("Usuário já cadastrado");
        }

        if (!password.equals(passwordConfirmation)) {
            throw new ConflictException("As senhas não são iguais");
        }

        User user = modelMapper.map(payload, User.class);
        userRepository.save(user);
    }
}
