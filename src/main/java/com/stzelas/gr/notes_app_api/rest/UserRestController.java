package com.stzelas.gr.notes_app_api.rest;

import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectAlreadyExists;
import com.stzelas.gr.notes_app_api.core.exceptions.AppServerException;
import com.stzelas.gr.notes_app_api.core.exceptions.ValidationException;
import com.stzelas.gr.notes_app_api.dto.UserInsertDTO;
import com.stzelas.gr.notes_app_api.dto.UserReadOnlyDTO;
import com.stzelas.gr.notes_app_api.mapper.Mapper;
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
    private final Mapper mapper;

    @PostMapping("/users/register")
    public ResponseEntity<UserReadOnlyDTO> saveUser(
            @Valid @RequestPart(name = "user")UserInsertDTO userInsertDTO,
            BindingResult bindingResult) throws ValidationException, AppObjectAlreadyExists, AppServerException {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        try {
            UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDTO);
            return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
        } catch (AppObjectAlreadyExists e) {
            LOGGER.error("Error in saving user because it already exists: ", e);
            throw new AppServerException("User", "User could not be registered.");
        }
    }
}
