
package com.ecobank.core.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;

  private String address;

  @PositiveOrZero
  private Double income;

  @Email
  private String email;

  @Pattern(regexp = "^[0-9\\-+() ]{7,20}$")
  private String phone;

  // Bidirectional link to accounts (optional)
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Account> accounts = new ArrayList<>();

  // Bidirectional link to opportunities (optional)
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<CustomerOpportunity> opportunities = new ArrayList<>();

    private String segment; // Retail / Premium

    @ElementCollection
    @CollectionTable(name = "customer_consents")
    private Set<String> consents;

    @Column(length = 2000)
    private String preferences;

    @OneToMany(mappedBy = "customer")
    private List<Redemption> redemptions;

    @OneToMany(mappedBy = "customer")
    private List<WalletEntry> walletEntries;

}
