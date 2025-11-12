package com.petaByte.miApp.controller;

import com.petaByte.miApp.model.Product;
import com.petaByte.miApp.service.ProductService;
import com.petaByte.miApp.service.FileStorageService; // IMPORTANTE: Importar el nuevo servicio
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileStorageService fileStorageService; // Inyectar el servicio de archivos

    // === Crear producto (MODIFICADO para aceptar archivos) ===
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") String productStr,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        try {
            // Convertir el string del producto a un objeto Product
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productStr, Product.class);

            // Si se sube un archivo, guardarlo y obtener la URL
            if (file != null && !file.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(file);
                product.setImagen(imageUrl);
            }

            Product saved = productService.saveProduct(product);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception e) {
            // Considera un manejo de errores más específico aquí
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // === Obtener todos los productos ===
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // === Obtener producto por ID ===
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // === Actualizar producto ===
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    // === Eliminar producto ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // === Búsqueda avanzada de productos ===
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) Boolean envioGratis,
            @RequestParam(required = false) Boolean descuento,
            @RequestParam(required = false) String orden
    ) {
        List<Product> results = productService.search(q, categoria, min, max, envioGratis, descuento, orden);
        return ResponseEntity.ok(results);
    }
}