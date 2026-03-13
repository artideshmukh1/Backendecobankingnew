// com/ecobank/core/models/AppUser.java
package com.ecobank.core.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String name;
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Link to Customer — null for BANK_ADMIN and PLAYER
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public enum Role {
        BANK_ADMIN, CUSTOMER, PLAYER
    }
}
