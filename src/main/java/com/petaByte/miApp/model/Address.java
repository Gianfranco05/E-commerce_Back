package com.petaByte.miApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String province;

    @NotBlank
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore // Para evitar problemas al convertir a JSON
    private User user;

    // Constructor para facilitar la creación
    public Address(String street, String city, String province, String postalCode, User user) {
        this.street = street;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.user = user;
    }
}