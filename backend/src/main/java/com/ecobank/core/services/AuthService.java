// com/ecobank/core/service/AuthService.java
package com.ecobank.core.services;

import com.ecobank.core.Repository.AppUserRepository;
import com.ecobank.core.configs.JwtUtil;
import com.ecobank.core.models.AppUser;
import com.ecobank.core.configs.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public String login(String email, String rawPassword) {
        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, appUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(appUser);
    }

    public AppUser register(String email, String password, String name, AppUser.Role role) {
        AppUser user = AppUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .role(role)
                .build();
        return userRepository.save(user);
    }
}