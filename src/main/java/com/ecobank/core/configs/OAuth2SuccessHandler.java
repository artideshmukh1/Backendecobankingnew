package com.ecobank.core.configs;

// com/ecobank/core/security/OAuth2SuccessHandler.java

import com.ecobank.core.models.AppUser;
import com.ecobank.core.models.Customer;
import com.ecobank.core.Repository.AppUserRepository;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.ecobank.core.Repository.CustomerRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email   = oAuth2User.getAttribute("email");
        String name    = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        // Find existing AppUser OR create new one
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseGet(() -> {
                    // Create a linked Customer record for new users
                    Customer customer = new Customer();
                    customer.setEmail(email);
                    customer.setName(name);
                    Customer savedCustomer = customerRepository.save(customer);

                    return appUserRepository.save(
                            AppUser.builder()
                                    .email(email)
                                    .name(name)
                                    .picture(picture)
                                    .role(AppUser.Role.CUSTOMER) // default role
                                    .customer(savedCustomer)
                                    .build()
                    );
                });

        // Generate JWT
        String token = jwtUtil.generateToken(appUser);

        //  Send token to Angular via URL param
        getRedirectStrategy().sendRedirect(request, response,
                "http://localhost:4200?token=" + token);
    }
}