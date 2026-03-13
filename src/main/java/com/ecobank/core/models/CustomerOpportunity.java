
package com.ecobank.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "customer_opportunities")
@Data
public class CustomerOpportunity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false)

  @JsonIgnore
  private Customer customer;   // <-- correct association to Customer entity

  // Using FK only for Product to avoid missing Product class for now.
  @NotNull
  @Column(name = "product_id", nullable = false)
  private Long productId;

  @NotBlank
  @Column(nullable = false)
  private String assignedStaff;

  @NotBlank
  @Column(nullable = false)
  private String status; // e.g., NEW, CONTACTED, WON, LOST, FULFILLED
}

