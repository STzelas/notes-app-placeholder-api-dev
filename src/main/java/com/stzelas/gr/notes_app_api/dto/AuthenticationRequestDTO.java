package com.stzelas.gr.notes_app_api.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequestDTO (
        @NotNull
        String username,
        @NotNull
        String password
) {}
