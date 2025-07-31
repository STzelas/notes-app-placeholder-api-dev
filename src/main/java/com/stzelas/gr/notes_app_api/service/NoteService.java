package com.stzelas.gr.notes_app_api.service;

import com.stzelas.gr.notes_app_api.dto.NoteInsertDTO;
import com.stzelas.gr.notes_app_api.dto.NoteReadOnlyDTO;
import com.stzelas.gr.notes_app_api.mapper.Mapper;
import com.stzelas.gr.notes_app_api.model.Note;
import com.stzelas.gr.notes_app_api.repository.NoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {


    private final NoteRepository noteRepository;
    private final Mapper mapper;

    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    @Transactional(rollbackOn = Exception.class)
    public NoteReadOnlyDTO saveNote(NoteInsertDTO noteInsertDTO) {

        Note note = mapper.mapToNoteEntity(noteInsertDTO);
        Note savedNote = noteRepository.save(note);
        return mapper.mapToNoteReadOnlyDTO(savedNote);
    }

    @Transactional(rollbackOn = Exception.class)
    public NoteReadOnlyDTO updateNote(NoteInsertDTO noteInsertDTO) {
        Note note = mapper.mapToNoteEntity(noteInsertDTO);
        Note savedNote = noteRepository.save(note);
        return mapper.mapToNoteReadOnlyDTO(savedNote);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

}
