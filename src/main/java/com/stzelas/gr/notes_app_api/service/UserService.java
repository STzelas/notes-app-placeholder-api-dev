package com.stzelas.gr.notes_app_api.service;

import com.stzelas.gr.notes_app_api.core.exceptions.AppObjectAlreadyExists;
import com.stzelas.gr.notes_app_api.dto.UserInsertDTO;
import com.stzelas.gr.notes_app_api.dto.UserReadOnlyDTO;
import com.stzelas.gr.notes_app_api.mapper.Mapper;
import com.stzelas.gr.notes_app_api.model.User;
import com.stzelas.gr.notes_app_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = {AppObjectAlreadyExists.class})
    public UserReadOnlyDTO register(UserInsertDTO userInsertDTO) throws AppObjectAlreadyExists {
        if (userRepository.findByUsername(userInsertDTO.username()) != null) {
            throw new AppObjectAlreadyExists("User", "User with username " + userInsertDTO.username() + " already exists.");
        }

        User user = mapper.mapToUserEntity(userInsertDTO);
        User savedUser = userRepository.save(user);

        return mapper.mapToUserReadOnlyDTO(savedUser);
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

}
