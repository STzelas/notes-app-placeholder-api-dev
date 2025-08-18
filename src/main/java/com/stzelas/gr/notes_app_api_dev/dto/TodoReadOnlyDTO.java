package com.stzelas.gr.notes_app_api_dev.dto;

import com.stzelas.gr.notes_app_api_dev.core.enums.Importance;

public record TodoReadOnlyDTO(
        Long id,
        String description,
        Importance importance,
        Boolean isComplete
) {
}
