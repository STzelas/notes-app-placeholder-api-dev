package com.stzelas.gr.notes_app_api_dev.authentication;

import com.stzelas.gr.notes_app_api_dev.model.User;
import com.stzelas.gr.notes_app_api_dev.model.UserPrincipal;
import com.stzelas.gr.notes_app_api_dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }

        return new UserPrincipal(user);
    }
}
