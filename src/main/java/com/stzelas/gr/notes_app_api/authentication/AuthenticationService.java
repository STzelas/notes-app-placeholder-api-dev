package com.stzelas.gr.notes_app_api.authentication;

import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api.dto.AuthenticationRequestDTO;
import com.stzelas.gr.notes_app_api.dto.AuthenticationResponseDTO;
import com.stzelas.gr.notes_app_api.model.User;
import com.stzelas.gr.notes_app_api.repository.UserRepository;
import com.stzelas.gr.notes_app_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            throw new AppObjectNotAuthorizedException("User", "User not authorized");
        }
        String token = jwtService.generateToken(dto.username(), dto.password());
        return new AuthenticationResponseDTO(user.getFirstname(), user.getLastname(), token);

    }
}