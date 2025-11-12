package com.petaByte.miApp.repository;

import com.petaByte.miApp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Método para buscar una categoría por su nombre
    Optional<Category> findByName(String name);
}