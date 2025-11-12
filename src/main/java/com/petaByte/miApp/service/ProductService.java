package com.petaByte.miApp.service;

import com.petaByte.miApp.model.Product;
import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    // Nueva función para búsqueda avanzada
    List<Product> search(String q, String categoria, Double min, Double max,
                         Boolean envioGratis, Boolean descuento, String orden);
}
