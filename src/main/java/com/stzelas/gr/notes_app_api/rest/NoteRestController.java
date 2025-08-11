package com.stzelas.gr.notes_app_api.rest;

import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api.core.exceptions.AppServerException;
import com.stzelas.gr.notes_app_api.core.exceptions.ValidationException;
import com.stzelas.gr.notes_app_api.dto.NoteInsertDTO;
import com.stzelas.gr.notes_app_api.dto.NoteReadOnlyDTO;
import com.stzelas.gr.notes_app_api.model.User;
import com.stzelas.gr.notes_app_api.model.UserPrincipal;
import com.stzelas.gr.notes_app_api.service.NoteService;
import com.stzelas.gr.notes_app_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;
    private final Logger LOGGER = LoggerFactory.getLogger(NoteRestController.class);
    private final UserService userService;

    @GetMapping("/notes")
    public List<NoteReadOnlyDTO> getNotes(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.findByUsername(principal.getUsername());
        return noteService.getNotes(user);
    }

    @PostMapping("/notes/save")
    public ResponseEntity<NoteReadOnlyDTO> saveNote(@Valid @RequestBody()NoteInsertDTO noteInsertDTO,
                                                    @AuthenticationPrincipal UserPrincipal principal)
                            throws ValidationException, AppServerException {

        try {
            NoteReadOnlyDTO savedNote = noteService.saveNote(noteInsertDTO, principal.getUser());
            LOGGER.info("Note saved successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
        } catch (Exception e) {
            LOGGER.error("Error could not be saved...: ", e);
            throw new AppServerException("Note", "Note could not be created.");
        }
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<NoteReadOnlyDTO> updateNote (
            @PathVariable Long id,
            @RequestBody NoteInsertDTO noteInsertDTO,
            @AuthenticationPrincipal UserPrincipal principal) throws AppObjectNotAuthorizedException {

        User user = userService.findByUsername(principal.getUsername());
        NoteReadOnlyDTO updatedNote = noteService.updateNote(id, noteInsertDTO, user);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(
            @PathVariable("id")Long noteId,
            @AuthenticationPrincipal UserPrincipal principal) {

        User user = userService.findByUsername(principal.getUsername()); // 204
        try {
            noteService.deleteNote(noteId, user);
            LOGGER.info("User {} deleted note with id {}", user.getUsername(), noteId);

            // Return a JSON response with a message
            return ResponseEntity.ok().body(
                    Map.of("message", "Note deleted successfully", "noteId", noteId)
            );
        } catch (AppObjectNotAuthorizedException e) {
            LOGGER.warn("User {} attempted to delete unauthorized or non-existent note with id {}",
                    user.getUsername(), noteId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You are not authorized to delete this note"));
        }
    }
}
