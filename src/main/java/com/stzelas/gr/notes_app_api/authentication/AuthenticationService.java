package com.stzelas.gr.notes_app_api.authentication;

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


    public String authenticate(AuthenticationRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        if (authentication.isAuthenticated())
            return jwtService.generateToken(dto.username(), dto.password());
        return "fail";
    }
}