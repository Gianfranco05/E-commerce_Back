package com.petaByte.miApp.repository;

import com.petaByte.miApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:q IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
            "(:categoria IS NULL OR LOWER(p.category.name) = LOWER(:categoria)) AND " +
            "(:min IS NULL OR p.price >= :min) AND " +
            "(:max IS NULL OR p.price <= :max) AND " +
            "(:envioGratis IS NULL OR p.envioGratis = :envioGratis) AND " +
            "(:descuento IS NULL OR p.descuento = :descuento)")
    List<Product> search(
            @Param("q") String q,
            @Param("categoria") String categoria,
            @Param("min") Double min,
            @Param("max") Double max,
            @Param("envioGratis") Boolean envioGratis,
            @Param("descuento") Boolean descuento
    );
}
