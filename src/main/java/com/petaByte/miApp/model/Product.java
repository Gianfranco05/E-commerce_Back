package com.petaByte.miApp.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;

    private String description;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    private Boolean descuento;

    private Boolean envioGratis;

    // --- NUEVO CAMPO AÑADIDO ---
    // Guardará la URL de la imagen del producto
    @Column(length = 2048) // Longitud generosa para URLs largas
    private String imagen;

    // --- RELACIÓN AÑADIDA ---
    // Muchos productos pueden pertenecer a una categoría.
    @ManyToOne(fetch = FetchType.EAGER) // EAGER para que siempre traiga la categoría
    @JoinColumn(name = "category_id")   // Nombre de la columna en la tabla 'products'
    private Category category;
}