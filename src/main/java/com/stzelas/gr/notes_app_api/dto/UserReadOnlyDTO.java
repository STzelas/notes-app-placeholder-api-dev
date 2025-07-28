package com.stzelas.gr.notes_app_api.dto;

import com.stzelas.gr.notes_app_api.core.enums.Role;

public record UserReadOnlyDTO(
        String username,
        String firstname,
        String lastname
) {
}
