package com.stzelas.gr.notes_app_api.dto;

public record UserReadOnlyDTO(
        String username,
        String firstname,
        String lastname
) {
}
