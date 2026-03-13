// com/ecobank/core/controller/AuthController.java
package com.ecobank.core.controllers;
import com.ecobank.core.models.AppUser;
import com.ecobank.core.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ── Login ──────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.email(), req.password());
        return ResponseEntity.ok(Map.of("token", token));
    }

    // ── Register ───────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        AppUser user = authService.register(req.email(), req.password(), req.name(), req.role());
        return ResponseEntity.ok(Map.of("message", "User registered", "email", user.getEmail()));
    }

    // ── Records (Java 16+) ─────────────────────────────
    public record LoginRequest(String email, String password) {}
    public record RegisterRequest(String email, String password, String name, AppUser.Role role) {}
}