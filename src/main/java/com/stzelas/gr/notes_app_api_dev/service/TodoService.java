package com.stzelas.gr.notes_app_api_dev.service;


import com.stzelas.gr.notes_app_api_dev.core.exceptions.AppObjectNotAuthorizedException;
import com.stzelas.gr.notes_app_api_dev.dto.TodoInsertDTO;
import com.stzelas.gr.notes_app_api_dev.dto.TodoReadOnlyDTO;
import com.stzelas.gr.notes_app_api_dev.mapper.Mapper;
import com.stzelas.gr.notes_app_api_dev.model.Todo;
import com.stzelas.gr.notes_app_api_dev.model.User;
import com.stzelas.gr.notes_app_api_dev.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final Mapper mapper;

    public List<TodoReadOnlyDTO> getTodos(User user) {
        List<Todo> todos = todoRepository.findByUserId(user.getId());
        return todos.stream()
                .map(mapper::mapToTodoReadOnlyDTO)
                .toList();
    }

    @Transactional(rollbackOn = Exception.class)
    public TodoReadOnlyDTO saveTodo(TodoInsertDTO todoInsertDTO, User user) {

        Todo todo = mapper.mapToTodoEntity(todoInsertDTO, user);
        Todo savedTodo = todoRepository.save(todo);
        return mapper.mapToTodoReadOnlyDTO(savedTodo);
    }

    @Transactional(rollbackOn = Exception.class)
    public TodoReadOnlyDTO updateTodo(Long todoId, TodoInsertDTO todoInsertDTO, User user) throws AppObjectNotAuthorizedException {
        Todo existingTodo = todoRepository.findByIdAndUserId(todoId, user.getId()).orElseThrow(() -> new AppObjectNotAuthorizedException("Todo", "Not found or authorized"));

        if (!existingTodo.getUser().getId().equals(user.getId())) {
            throw new AppObjectNotAuthorizedException("Todo", "Not owned by user");
        }
        existingTodo.setDescription(todoInsertDTO.description());
        existingTodo.setImportance(todoInsertDTO.importance());
        existingTodo.setIsCompleted(todoInsertDTO.isCompleted());

        Todo savedTodo = todoRepository.save(existingTodo);
        return mapper.mapToTodoReadOnlyDTO(savedTodo);
    }

    public void deleteTodo(Long todoId, User user) throws AppObjectNotAuthorizedException {
        Todo todo = todoRepository.findByIdAndUserId(todoId, user.getId())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("Todo", "Not found or authorized"));

        todoRepository.delete(todo);
    }
}
