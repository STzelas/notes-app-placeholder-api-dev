package com.stzelas.gr.notes_app_api.rest;

import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api.core.exceptions.AppServerException;
import com.stzelas.gr.notes_app_api.dto.NoteInsertDTO;
import com.stzelas.gr.notes_app_api.dto.NoteReadOnlyDTO;
import com.stzelas.gr.notes_app_api.model.User;
import com.stzelas.gr.notes_app_api.model.UserPrincipal;
import com.stzelas.gr.notes_app_api.service.NoteService;
import com.stzelas.gr.notes_app_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(
            summary = "Get all notes from principal",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get all notes from logged in user.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NoteReadOnlyDTO.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @Tag(name = "Get all notes")
    @GetMapping("/notes")
    public List<NoteReadOnlyDTO> getNotes(@AuthenticationPrincipal UserPrincipal principal) throws AppServerException {
        try {
            User user = userService.findByUsername(principal.getUsername());
            return noteService.getNotes(user);
        } catch (Exception e) {
            LOGGER.error("Error getting all the notes: ", e);
            throw new AppServerException("Note", "Error in getting the notes");
        }
    }


    @Operation(
            summary = "Save a note",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Note created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NoteReadOnlyDTO.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @Tag(name = "Save a Note")
    @PostMapping("/notes/save")
    public ResponseEntity<NoteReadOnlyDTO> saveNote(@Valid @RequestBody()NoteInsertDTO noteInsertDTO,
                                                    @AuthenticationPrincipal UserPrincipal principal)
                            throws AppServerException {

        try {
            NoteReadOnlyDTO savedNote = noteService.saveNote(noteInsertDTO, principal.getUser());
            LOGGER.info("Note saved successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
        } catch (Exception e) {
            LOGGER.error("Error could not be saved...: ", e);
            throw new AppServerException("Note", "Note could not be created.");
        }
    }

    @Operation(
            summary = "Updates a specified note.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updates the modified properties from the specified note.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NoteReadOnlyDTO.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @Tag(name = "Update a Note")
    @PutMapping("/notes/{noteId}")
    public ResponseEntity<NoteReadOnlyDTO> updateNote (
            @PathVariable Long noteId,
            @RequestBody NoteInsertDTO noteInsertDTO,
            @AuthenticationPrincipal UserPrincipal principal) throws AppServerException {

        try {
            User user = userService.findByUsername(principal.getUsername());
            NoteReadOnlyDTO updatedNote = noteService.updateNote(noteId, noteInsertDTO, user);
            LOGGER.info("Note updated successfully.");
            return ResponseEntity.ok(updatedNote);
        } catch (Exception e) {
            LOGGER.error("Error. Note could not be updated...: ", e);
            throw new AppServerException("Note", "Note could not be updated.");
        }

    }


    @Operation(
            summary = "Deletes a specified note.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deletes a specified note.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NoteReadOnlyDTO.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @Tag(name = "Delete a Note")
    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<?> deleteNote(
            @PathVariable("noteId")Long noteId,
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
                    .body(Map.of("error", "You are not authorized to delete this note, or it does not exists."));
        }
    }
}
