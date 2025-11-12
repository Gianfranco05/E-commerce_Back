package com.petaByte.miApp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name ="roles")

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usamos EnumType.STRING para guardar "ROLE_USER" o "ROLE_ADMIN" como texto.
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleName name;
}
