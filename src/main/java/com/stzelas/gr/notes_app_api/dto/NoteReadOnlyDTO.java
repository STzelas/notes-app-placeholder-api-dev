package com.stzelas.gr.notes_app_api.dto;

public record NoteReadOnlyDTO (
        Long id,
        String title,
        String content
){
}
