package com.stzelas.gr.notes_app_api.service;

import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api.dto.NoteInsertDTO;
import com.stzelas.gr.notes_app_api.dto.NoteReadOnlyDTO;
import com.stzelas.gr.notes_app_api.mapper.Mapper;
import com.stzelas.gr.notes_app_api.model.Note;
import com.stzelas.gr.notes_app_api.model.User;
import com.stzelas.gr.notes_app_api.repository.NoteRepository;
import com.stzelas.gr.notes_app_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {


    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    public List<NoteReadOnlyDTO> getNotes(User user) {
        List<Note> notes = noteRepository.findByUserId(user.getId());
        return notes.stream()
                .map(mapper::mapToNoteReadOnlyDTO)
                .toList();
    }

    @Transactional(rollbackOn = Exception.class)
    public NoteReadOnlyDTO saveNote(NoteInsertDTO noteInsertDTO, User user) {

        Note note = mapper.mapToNoteEntity(noteInsertDTO, user);
        Note savedNote = noteRepository.save(note);
        return mapper.mapToNoteReadOnlyDTO(savedNote);
    }

    @Transactional(rollbackOn = Exception.class)
    public NoteReadOnlyDTO updateNote(Long noteId, NoteInsertDTO noteInsertDTO, User user) throws AppObjectNotAuthorizedException {
        Note existingNote = noteRepository.findByIdAndUserId(noteId, user.getId()).orElseThrow(() -> new AppObjectNotAuthorizedException("Note", "Not found or authorized"));

        if (!existingNote.getUser().getId().equals(user.getId())) {
            throw new AppObjectNotAuthorizedException("Note", "Not owned by user");
        }
        existingNote.setTitle(noteInsertDTO.title());
        existingNote.setContent(noteInsertDTO.content());

        Note savedNote = noteRepository.save(existingNote);
        return mapper.mapToNoteReadOnlyDTO(savedNote);
    }

    public void deleteNote(Long noteId, User user) throws AppObjectNotAuthorizedException {
        Note note = noteRepository.findByIdAndUserId(noteId, user.getId())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("Note", "Not found or authorized"));

        noteRepository.delete(note);
    }

}
