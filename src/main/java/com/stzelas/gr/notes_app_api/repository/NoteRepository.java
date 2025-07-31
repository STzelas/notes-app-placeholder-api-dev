package com.stzelas.gr.notes_app_api.repository;

import com.stzelas.gr.notes_app_api.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
