package com.stzelas.gr.notes_app_api.dto;

import com.stzelas.gr.notes_app_api.core.enums.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserInsertDTO(

        @NotEmpty(message = "Το username δεν μπορεί να είναι κενό")
        @Size(min = 4, max = 15, message = "Το username πρέπει να είναι μεταξύ 4 και 15 χαρακτήρων")
        String username,
        @NotEmpty(message = "Το password δεν μπορεί να είναι κενό")
        @Size(min = 4, max = 15, message = "Το password πρέπει να είναι τουλάχιστον 6 χαρακτήρες")
        String password,
        @NotEmpty(message = "Το όνομα δεν μπορεί να είναι κενό")
        @Size(min = 4, max = 15, message = "Το password όνομα να είναι τουλάχιστον 3 χαρακτήρες")
        String firstname,
        @NotEmpty(message = "Το επίθετο δεν μπορεί να είναι κενό")
        @Size(min = 4, max = 15, message = "Το επίθετο πρέπει να είναι τουλάχιστον 3 χαρακτήρες")
        String lastname,

        Role role
) {}
