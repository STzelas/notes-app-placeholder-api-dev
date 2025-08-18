package com.stzelas.gr.notes_app_api_dev.model;

import com.stzelas.gr.notes_app_api_dev.core.enums.Importance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "todos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Todo extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description;

    @Column(name = "is_completed")
    Boolean isCompleted;

    @Enumerated(value = EnumType.STRING)
    Importance importance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // owner of note
}
