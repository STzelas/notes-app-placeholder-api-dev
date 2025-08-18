package com.stzelas.gr.notes_app_api_dev.rest;

import com.stzelas.gr.notes_app_api_dev.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api_dev.core.exceptions.AppServerException;
import com.stzelas.gr.notes_app_api_dev.dto.NoteInsertDTO;
import com.stzelas.gr.notes_app_api_dev.dto.NoteReadOnlyDTO;
import com.stzelas.gr.notes_app_api_dev.model.User;
import com.stzelas.gr.notes_app_api_dev.model.UserPrincipal;
import com.stzelas.gr.notes_app_api_dev.service.NoteService;
import com.stzelas.gr.notes_app_api_dev.service.UserService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Note Rest Controller")
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
    @PostMapping("/notes/save")
    public ResponseEntity<?> saveNote(@Valid
                                      @RequestBody()NoteInsertDTO noteInsertDTO,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal UserPrincipal principal) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Validation failed. Title and Content must not be null.")
            );
        }

        try {
            NoteReadOnlyDTO savedNote = noteService.saveNote(noteInsertDTO, principal.getUser());
            LOGGER.info("Note saved successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
        } catch (Exception e) {
            LOGGER.error("Error could not be saved...: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "There was a problem saving your note"));
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
    @PutMapping("/notes/{noteId}")
    public ResponseEntity<?> updateNote (
            @Valid
            @PathVariable Long noteId,
            @RequestBody NoteInsertDTO noteInsertDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal principal) throws AppServerException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Validation failed. Title and Content must not be null.")
            );
        }

        try {
            User user = userService.findByUsername(principal.getUsername());
            NoteReadOnlyDTO updatedNote = noteService.updateNote(noteId, noteInsertDTO, user);
            LOGGER.info("Note updated successfully.");
            return ResponseEntity.ok(updatedNote);
        } catch (AppObjectNotAuthorizedException e) {
        LOGGER.warn("User {} attempted to update unauthorized or non-existent note with id {}",
                principal.getUsername(), noteId);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "You are not authorized to delete this note, or it does not exists."));
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
