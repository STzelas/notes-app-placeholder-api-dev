package com.stzelas.gr.notes_app_api_dev.rest;

import com.stzelas.gr.notes_app_api_dev.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api_dev.core.exceptions.AppServerException;
import com.stzelas.gr.notes_app_api_dev.dto.TodoInsertDTO;
import com.stzelas.gr.notes_app_api_dev.dto.TodoReadOnlyDTO;
import com.stzelas.gr.notes_app_api_dev.model.User;
import com.stzelas.gr.notes_app_api_dev.model.UserPrincipal;
import com.stzelas.gr.notes_app_api_dev.service.TodoService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Todo Rest Controller")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoRestController {

    private final TodoService todoService;
    private final UserService userService;
    private final Logger LOGGER = LoggerFactory.getLogger(TodoRestController.class);

    @Operation(
            summary = "Get all todos from principal",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get all todos from logged in user.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TodoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/todos")
    public List<TodoReadOnlyDTO> getTodos(@AuthenticationPrincipal UserPrincipal principal) throws AppServerException {
        try {
            User user = userService.findByUsername(principal.getUsername());
            return todoService.getTodos(user);
        } catch (Exception e) {
            LOGGER.error("Error getting todo list: ", e);
            throw new AppServerException("Todo", "Error in getting the todo list");
        }
    }

    @Operation(
            summary = "Save a todo",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Todo created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TodoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/todos/save")
    public ResponseEntity<TodoReadOnlyDTO> saveTodo(@Valid @RequestBody() TodoInsertDTO todoInsertDTO,
                                                    @AuthenticationPrincipal UserPrincipal principal)
            throws AppServerException {

        try {
            TodoReadOnlyDTO savedTodo = todoService.saveTodo(todoInsertDTO, principal.getUser());
            LOGGER.info("Todo saved successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTodo);
        } catch (Exception e) {
            LOGGER.error("Error, could not be saved...: ", e);
            throw new AppServerException("Todo", "Todo could not be created.");
        }
    }

    @Operation(
            summary = "Updates a specified todo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updates the modified properties from the specified todo.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TodoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/todos/{todoId}")
    public ResponseEntity<TodoReadOnlyDTO> updateTodo (
            @PathVariable Long todoId,
            @RequestBody TodoInsertDTO todoInsertDTO,
            @AuthenticationPrincipal UserPrincipal principal) throws AppServerException {

        try {
            User user = userService.findByUsername(principal.getUsername());
            TodoReadOnlyDTO updatedTodo = todoService.updateTodo(todoId, todoInsertDTO, user);
            LOGGER.info("Todo updated successfully.");
            return ResponseEntity.ok(updatedTodo);
        } catch (Exception e) {
            LOGGER.error("Error. Todo could not be updated...: ", e);
            throw new AppServerException("Todo", "Todo could not be updated.");
        }
    }

    @Operation(
            summary = "Deletes a specified todo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deletes a specified todo.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TodoReadOnlyDTO.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<?> deleteTodo(
            @PathVariable("todoId")Long todoId,
            @AuthenticationPrincipal UserPrincipal principal) {

        User user = userService.findByUsername(principal.getUsername());
        try {
            todoService.deleteTodo(todoId, user);
            LOGGER.info("User {} deleted todo with id {}", user.getUsername(), todoId);

            // Return a JSON response with a message
            return ResponseEntity.ok().body(
                    Map.of("message", "Todo deleted successfully", "todoId", todoId)
            );
        } catch (AppObjectNotAuthorizedException e) {
            LOGGER.warn("User {} attempted to delete unauthorized or non-existent todo with id {}",
                    user.getUsername(), todoId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You are not authorized to delete this todo, or it does not exists."));
        }
    }

}
