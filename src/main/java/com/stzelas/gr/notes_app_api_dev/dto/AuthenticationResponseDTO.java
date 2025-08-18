package com.stzelas.gr.notes_app_api_dev.dto;


public record AuthenticationResponseDTO (
        String firstname,
        String lastname,
        String token
) {}
