package com.stzelas.gr.notes_app_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteInsertDTO(
        @NotNull(message = "Title must not be empty.")
        @Size(min = 3, message = "Title must be at least 3 characters long.")
        String title,

        @NotNull(message = "Note content must not be empty.")
        @Size(min = 3, message = "Content must be at least 3 characters long.")
        String content
) {}
