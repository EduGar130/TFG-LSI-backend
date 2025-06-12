package com.tfg.security.service;

import com.tfg.entity.User;
import com.tfg.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        String perms = user.getRole().getPermissions();
        if (perms != null && !perms.isBlank()) {
            perms = perms.replace("[", "").replace("]", "").replace("\"", "");
            Arrays.stream(perms.split(","))
                    .map(String::trim)
                    .filter(p -> !p.isEmpty())
                    .forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                authorities
        );
    }
}
