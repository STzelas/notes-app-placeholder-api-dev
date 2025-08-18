package com.stzelas.gr.notes_app_api_dev.rest;

import com.stzelas.gr.notes_app_api_dev.authentication.AuthenticationService;
import com.stzelas.gr.notes_app_api_dev.core.exceptions.AppObjectAlreadyExists;
import com.stzelas.gr.notes_app_api_dev.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api_dev.core.exceptions.ValidationException;
import com.stzelas.gr.notes_app_api_dev.dto.*;
import com.stzelas.gr.notes_app_api_dev.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(
            summary = "Registers a User",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserReadOnlyDTO.class)
                            )
                    )
            }
    )
    @Tag(name = "Register User")
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

    @Operation(
            summary = "Logs a user in/",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User Log In.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDTO.class)
                            )
                    )
            }
    )
    @Tag(name = "Log In")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO requestDTO)
            throws AppObjectNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authService.authenticate(requestDTO);
        LOGGER.info("User authenticated");
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
    }
}
