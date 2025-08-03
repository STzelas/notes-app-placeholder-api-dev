package com.stzelas.gr.notes_app_api.dto;


public record AuthenticationResponseDTO (
        String firstname,
        String lastname,
        String token
) {}
