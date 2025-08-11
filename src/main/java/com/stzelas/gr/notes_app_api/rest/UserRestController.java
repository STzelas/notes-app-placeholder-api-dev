package com.stzelas.gr.notes_app_api.rest;

import com.stzelas.gr.notes_app_api.authentication.AuthenticationService;
import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectAlreadyExists;
import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api.core.exceptions.ValidationException;
import com.stzelas.gr.notes_app_api.dto.AuthenticationRequestDTO;
import com.stzelas.gr.notes_app_api.dto.AuthenticationResponseDTO;
import com.stzelas.gr.notes_app_api.dto.UserInsertDTO;
import com.stzelas.gr.notes_app_api.dto.UserReadOnlyDTO;
import com.stzelas.gr.notes_app_api.mapper.Mapper;
import com.stzelas.gr.notes_app_api.model.User;
import com.stzelas.gr.notes_app_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;
    private final AuthenticationService authService;
    private final Mapper mapper;

    @PostMapping("/users/register")
    public ResponseEntity<UserReadOnlyDTO> register(
            @Valid @RequestBody()UserInsertDTO userInsertDTO,
            BindingResult bindingResult) throws ValidationException, AppObjectAlreadyExists {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        try {
            UserReadOnlyDTO userReadOnlyDTO = userService.register(userInsertDTO);
            return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
        } catch (AppObjectAlreadyExists e) {
            LOGGER.error("Error in saving user because it already exists: ", e);
            throw new AppObjectAlreadyExists("User", "User already exists.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO requestDTO)
            throws AppObjectNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authService.authenticate(requestDTO);
        LOGGER.info("User authenticated");
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
    }
}
