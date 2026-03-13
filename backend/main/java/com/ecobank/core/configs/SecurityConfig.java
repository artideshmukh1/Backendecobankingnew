//
//// src/main/java/com/ecobank/core/configs/SecurityConfig.java
//package com.ecobank.core.configs;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.util.List;
//
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest; // <-- add this
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowedOrigins(List.of("http://localhost:4200"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
//
//    @Bean
//    @Order(0)
//    public SecurityFilterChain h2ConsoleSecurity(HttpSecurity http) throws Exception {
//        http
//            // Use Boot's matcher for H2 console (less error-prone than a raw string)
//            .securityMatcher(PathRequest.toH2Console())
//            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//            .csrf(csrf -> csrf.disable())                    // H2 console issues POSTs without CSRF
//            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // disable frames for safety
//
//        return http.build();
//    }
//
//    @Bean
//    @Order(1)
//    public SecurityFilterChain appSecurity(HttpSecurity http) throws Exception {
//    http
//    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//    // CSRF: disable ONLY for API
//    .csrf(csrf -> csrf
//        .ignoringRequestMatchers("/api/**","/v1/**")
//    )
//    .authorizeHttpRequests(auth -> auth
//    .requestMatchers("/", "/login", "/login/oauth2/**", "/css/**", "/js/**",
//    "/images/**", "/error")
//    .permitAll()
//    .requestMatchers("/api/auth/logout").permitAll().requestMatchers("/v1/**").permitAll() //Change BY ARTI
//    .requestMatchers("/ui/**").authenticated()
//    .requestMatchers("/api/**").authenticated() // Change BY SHUBHAM
//    .anyRequest().permitAll()
//    )
//    .oauth2Login(oauth -> oauth
//    .loginPage("/login")
//    .defaultSuccessUrl("http://localhost:4200", true)
//    // .defaultSuccessUrl("/ui", true)
//    )
//    .logout(logout -> logout
//                .logoutUrl("/api/auth/logout")
//                .logoutSuccessHandler((req, res, auth) ->
//                        res.setStatus(HttpServletResponse.SC_OK))
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID")
//            );
//
//
//    // .csrf(Customizer.withDefaults()); // keep CSRF for app
//    return http.build();
//    }
//
//}



// com/ecobank/core/configs/SecurityConfig.java
package com.ecobank.core.configs;
//
//import com.ecobank.core.security.JwtAuthFilter;
//import com.ecobank.core.security.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@EnableMethodSecurity          // enables @PreAuthorize on controllers
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    @Order(0)
    public SecurityFilterChain h2ConsoleSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher(PathRequest.toH2Console())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(f -> f.disable()));
        return http.build();
    }

    // ── 1. Stateless JWT chain — handles all /api/** calls ──
    @Bean
    @Order(1)
    public SecurityFilterChain jwtSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**", "/v1/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**", "/api/token/generate", "/v1/**").permitAll()
                        .requestMatchers( "/api/token/generate").permitAll()

                        // ── Role-based API access ──────────────────────────
                        .requestMatchers("/api/admin/**").hasRole("BANK_ADMIN")
                        .requestMatchers("/api/player/**").hasRole("PLAYER")
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // ── 2. OAuth2 Google Login chain ──────────────────────
    @Bean
    @Order(2)
    public SecurityFilterChain appSecurity(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/v1/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/login/oauth2/**",
                                "/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(oAuth2SuccessHandler) // ✅ issues JWT after Google login
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((req, res, auth) ->
                                res.setStatus(HttpServletResponse.SC_OK))
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }
}