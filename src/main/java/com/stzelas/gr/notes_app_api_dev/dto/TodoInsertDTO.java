package com.stzelas.gr.notes_app_api_dev.dto;


import com.stzelas.gr.notes_app_api_dev.core.enums.Importance;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TodoInsertDTO(

        @NotNull(message = "Description must not be empty.")
        @Size(min = 3, message = "Description must be at least 3 characters long.")
        @Size(max = 255, message = "Description must not be more than 255 characters long.")
        String description,

        @NotNull
        Boolean isCompleted,

        @NotNull(message = "Importance must not be empty")
        @Schema(
                description = "Importance of the todo",
                example = "MINOR",
                allowableValues = {"MINOR", "MODERATE", "MAJOR"}
        )
        Importance importance
) {}
