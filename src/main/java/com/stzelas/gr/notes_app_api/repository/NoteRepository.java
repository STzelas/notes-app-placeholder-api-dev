package com.stzelas.gr.notes_app_api.repository;

import com.stzelas.gr.notes_app_api.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}
