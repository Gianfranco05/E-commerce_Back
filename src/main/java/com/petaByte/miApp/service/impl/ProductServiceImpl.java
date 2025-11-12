package com.petaByte.miApp.service.impl;

import com.petaByte.miApp.model.Product;
import com.petaByte.miApp.repository.ProductRepository;
import com.petaByte.miApp.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id " + id));
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setCategory(productDetails.getCategory());
        product.setDescuento(productDetails.getDescuento());
        product.setEnvioGratis(productDetails.getEnvioGratis());
        product.setImagen(productDetails.getImagen());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("No existe producto con id " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> search(String q, String categoria, Double min, Double max,
                                Boolean envioGratis, Boolean descuento, String orden) {

        // Llamamos al query del repository
        List<Product> results = productRepository.search(q, categoria, min, max, envioGratis, descuento);

        // Ordenamiento manual si se pasó el parámetro
        if (orden != null) {
            switch (orden.toLowerCase()) {
                case "precio_asc":
                    results.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
                    break;
                case "precio_desc":
                    results.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
                    break;
                // agregar más casos si necesitas otros órdenes
            }
        }

        return results;
    }
}
