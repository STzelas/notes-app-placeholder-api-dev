package com.stzelas.gr.notes_app_api_dev.model;

import com.stzelas.gr.notes_app_api_dev.core.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'USER'")
    private Role role;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();
}
