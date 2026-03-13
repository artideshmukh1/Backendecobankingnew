// com/ecobank/core/controllers/TokenController.java
package com.ecobank.core.controllers;

import com.ecobank.core.models.AppUser;
import com.ecobank.core.Repository.AppUserRepository;
import com.ecobank.core.configs.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;

    @PostMapping("/generate")
    public ResponseEntity<?> generateToken(@RequestBody TokenRequest request) {

        // Find existing user OR create new one
        AppUser user = appUserRepository.findByEmail(request.email())
                .orElseGet(() -> appUserRepository.save(
                        AppUser.builder()
                                .email(request.email())
                                .name(request.name())
                                .role(request.role())
                                .build()
                ));

        // Update role if you want to test different roles
        user.setRole(request.role());
        appUserRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole(),
                "email", user.getEmail()
        ));
    }

    public record TokenRequest(String email, String name, AppUser.Role role) {}
}

