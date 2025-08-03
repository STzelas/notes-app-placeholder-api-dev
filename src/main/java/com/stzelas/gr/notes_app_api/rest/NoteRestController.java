package com.stzelas.gr.notes_app_api.rest;

import com.stzelas.gr.notes_app_api.core.exceptions.AppServerException;
import com.stzelas.gr.notes_app_api.core.exceptions.ValidationException;
import com.stzelas.gr.notes_app_api.dto.NoteInsertDTO;
import com.stzelas.gr.notes_app_api.dto.NoteReadOnlyDTO;
import com.stzelas.gr.notes_app_api.model.Note;
import com.stzelas.gr.notes_app_api.model.User;
import com.stzelas.gr.notes_app_api.model.UserPrincipal;
import com.stzelas.gr.notes_app_api.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;
//    private final Logger LOGGER = LoggerFactory.getLogger(NoteRestController.class);


    @PostMapping("/notes/save")
    public ResponseEntity<NoteReadOnlyDTO> saveNote(@Valid @RequestBody()NoteInsertDTO noteInsertDTO,
                                                    @AuthenticationPrincipal UserPrincipal principal)
                            throws ValidationException, AppServerException {

        try {
            NoteReadOnlyDTO savedNote = noteService.saveNote(noteInsertDTO, principal.getUser());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
        } catch (Exception e) {
            throw new AppServerException("Note", "Note could not be created.");
        }
    }
}
