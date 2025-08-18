package com.stzelas.gr.notes_app_api_dev.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteInsertDTO(
        @NotNull(message = "Title must not be empty.")
        @Size(min = 1, message = "Title must not be empty")
        @Size(max = 255, message = "Title must not be more than 255 characters")
        String title,

        @NotNull(message = "Note content must not be empty.")
        @Size(min = 1, message = "Content must not be empty")
        @Size(max = 1000, message = "Content must not be more than 1000 characters")
        String content
) {}
