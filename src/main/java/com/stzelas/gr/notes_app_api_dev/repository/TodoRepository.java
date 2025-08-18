package com.stzelas.gr.notes_app_api_dev.repository;

import com.stzelas.gr.notes_app_api_dev.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserId(Long userId);
    Optional<Todo> findByIdAndUserId(Long todoId, Long userId);
}
