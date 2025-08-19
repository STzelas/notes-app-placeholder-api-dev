package com.stzelas.gr.notes_app_api_dev.mapper;

import com.stzelas.gr.notes_app_api_dev.core.enums.Role;
import com.stzelas.gr.notes_app_api_dev.dto.*;
import com.stzelas.gr.notes_app_api_dev.model.Note;
import com.stzelas.gr.notes_app_api_dev.model.Todo;
import com.stzelas.gr.notes_app_api_dev.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {


    private final PasswordEncoder passwordEncoder;

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
                user.getUsername(),
                user.getFirstname(),
                user.getLastname()
        );
    }

    public User mapToUserEntity(UserInsertDTO insertDTO) {
        User user = new User();
        user.setUsername(insertDTO.username());
        user.setPassword(passwordEncoder.encode(insertDTO.password()));
        user.setFirstname(insertDTO.firstname());
        user.setLastname(insertDTO.lastname());
        user.setRole(Role.USER); // Every user has USER role. Only one is SUPER_ADMIN, more roles might be added later.
        return user;
    }

    public NoteReadOnlyDTO mapToNoteReadOnlyDTO(Note note) {
        return new NoteReadOnlyDTO(
                note.getId(),
                note.getTitle(),
                note.getContent()
        );
    }

    public Note mapToNoteEntity(NoteInsertDTO noteInsertDTO, User user) {
        Note note = new Note();
        note.setTitle(noteInsertDTO.title());
        note.setContent(noteInsertDTO.content());
        note.setUser(user);
        return note;
    }


    public TodoReadOnlyDTO mapToTodoReadOnlyDTO(Todo todo) {
        return new TodoReadOnlyDTO(
                todo.getId(),
                todo.getDescription(),
                todo.getImportance(),
                todo.getIsCompleted()
        );
    }

    public Todo mapToTodoEntity(TodoInsertDTO todoInsertDTO, User user) {
        Todo todo = new Todo();
        todo.setDescription(todoInsertDTO.description());
        todo.setImportance(todoInsertDTO.importance());
        todo.setIsCompleted(false);  // All todos initially are not completed / false.
        todo.setUser(user);
        return todo;
    }

    public Todo mapToUpdatedTodoEntity(Todo todo, TodoUpdateDTO todoUpdateDTO) {
        todo.setDescription(todoUpdateDTO.description());
        todo.setImportance(todoUpdateDTO.importance());
        if (todoUpdateDTO.isCompleted() != null) {
            todo.setIsCompleted(todoUpdateDTO.isCompleted());
        }
        return todo;
    }

}
