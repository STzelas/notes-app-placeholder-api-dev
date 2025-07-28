package com.stzelas.gr.notes_app_api.service;

import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectAlreadyExists;
import com.stzelas.gr.notes_app_api.dto.UserInsertDTO;
import com.stzelas.gr.notes_app_api.dto.UserReadOnlyDTO;
import com.stzelas.gr.notes_app_api.mapper.Mapper;
import com.stzelas.gr.notes_app_api.model.User;
import com.stzelas.gr.notes_app_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    @Transactional(rollbackOn = {AppObjectAlreadyExists.class})
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws AppObjectAlreadyExists {
        if (userRepository.findByUsername(userInsertDTO.username()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with username " + userInsertDTO.username() + " already exists.");
        }

        User user = mapper.mapToUserEntity(userInsertDTO);
        User savedUser = userRepository.save(user);

        return mapper.mapToUserReadOnlyDTO(savedUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
