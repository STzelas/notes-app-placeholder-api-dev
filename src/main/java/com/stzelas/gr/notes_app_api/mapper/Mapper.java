package com.stzelas.gr.notes_app_api.mapper;

import com.stzelas.gr.notes_app_api.dto.UserInsertDTO;
import com.stzelas.gr.notes_app_api.dto.UserReadOnlyDTO;
import com.stzelas.gr.notes_app_api.model.User;
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
        return user;
    }

}
