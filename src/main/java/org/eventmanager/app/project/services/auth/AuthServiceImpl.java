package org.eventmanager.app.project.services.auth;

import org.eventmanager.app.project.exceptions.ConflictException;
import org.eventmanager.app.project.models.User;
import org.eventmanager.app.project.payload.request.auth.SignInRequestPayload;
import org.eventmanager.app.project.payload.request.auth.SignUpRequestPayload;
import org.eventmanager.app.project.payload.response.auth.SignInResponsePayload;
import org.eventmanager.app.project.payload.response.auth.UserInfoPayload;
import org.eventmanager.app.project.repositories.UserRepository;
import org.eventmanager.app.project.security.jwt.JwtUtils;
import org.eventmanager.app.project.security.services.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(ModelMapper modelMapper, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public SignInResponsePayload signIn(SignInRequestPayload payload) {
        String email = payload.getEmail();
        String password = payload.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookieFromUserDetails(userDetails);

        return new SignInResponsePayload(email, jwtCookie.toString());
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

        String encodedPassword = passwordEncoder.encode(password);
        payload.setPassword(encodedPassword);

        User user = modelMapper.map(payload, User.class);
        userRepository.save(user);
    }

    @Override
    public UserInfoPayload getUserInfoByUserDetails(UserDetailsImpl userDetails) {
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookieFromUserDetails(userDetails);

        UUID userId = userDetails.getId();
        String email = userDetails.getUsername();
        String token = jwtUtils.getJwtTokenFromCookieString(jwtCookie.toString());

        return new UserInfoPayload(userId, email, token);
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        return jwtUtils.getCleanJwtCookie();
    }
}
