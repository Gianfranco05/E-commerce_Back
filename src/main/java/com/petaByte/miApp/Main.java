package com.petaByte.miApp;

import com.petaByte.miApp.model.Category;
import com.petaByte.miApp.model.Product;
import com.petaByte.miApp.repository.CategoryRepository;
import com.petaByte.miApp.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository, CategoryRepository categoryRepository) {
        return args -> {
            // Solo se ejecuta si no hay categorías creadas
            if (categoryRepository.count() == 0) {
                System.out.println("Base de datos vacía. Cargando datos de ejemplo...");

                // 1. Crear y guardar las categorías primero
                Category perifericos = categoryRepository.save(new Category("Periféricos"));
                Category audio = categoryRepository.save(new Category("Audio"));
                Category componentes = categoryRepository.save(new Category("Componentes"));
                Category computadoras = categoryRepository.save(new Category("Computadoras"));
                Category monitores = categoryRepository.save(new Category("Monitores"));
                Category sillas = categoryRepository.save(new Category("Sillas"));

                // 2. Crear los productos, asignando la categoría correspondiente
                productRepository.save(Product.builder()
                        .name("Mouse Gamer")
                        .description("Mouse óptico con 7 botones y alta precisión para gaming.")
                        .price(24999.0)
                        .stock(20)
                        .imagen("https://redragon.es/content/uploads/2021/04/griffin-black-2.jpg")
                        .category(perifericos)
                        .build());

                productRepository.save(Product.builder()
                        .name("Barra de sonido")
                        .description("Potente barra de sonido para una experiencia de audio inmersiva.")
                        .price(89999.0)
                        .stock(12)
                        .imagen("https://redragon.es/content/uploads/2022/04/5-ESTILO-Y-ROBUSTEZ.jpg")
                        .category(audio)
                        .build());

                productRepository.save(Product.builder()
                        .name("Teclado RGB")
                        .description("Teclado mecánico con switches rojos e iluminación RGB personalizable.")
                        .price(45999.0)
                        .stock(15)
                        .imagen("https://i0.wp.com/www.aslanstoreuy.com/wp-content/uploads/2020/10/Teclado-Gamer-Redragon-Kumara-RGB-Aslan-Store-Uruguay-2.jpg?w=900&ssl=1")
                        .category(perifericos)
                        .build());

                productRepository.save(Product.builder()
                        .name("Cooler CPU RGB")
                        .description("Sistema de enfriamiento para CPU con ventilador RGB silencioso y eficiente.")
                        .price(32999.0)
                        .stock(30)
                        .imagen("https://redragon.es/content/uploads/2025/05/C1013-1.jpg")
                        .category(componentes)
                        .build());

                productRepository.save(Product.builder()
                        .name("Auriculares Gamer")
                        .description("Auriculares con sonido envolvente 7.1 y micrófono con cancelación de ruido.")
                        .price(38999.0)
                        .stock(25)
                        .imagen("https://dojiw2m9tvv09.cloudfront.net/86841/product/X_foto24207.jpg?68&time=1756745608")
                        .category(audio)
                        .build());

                productRepository.save(Product.builder()
                        .name("Notebook Gamer")
                        .description("Laptop de alta gama para los juegos más exigentes, con tarjeta gráfica dedicada.")
                        .price(299999.0)
                        .stock(5)
                        .imagen("https://guiadacompra.com/wp-content/uploads/2021/04/gamer-2.jpg")
                        .category(computadoras)
                        .build());

                productRepository.save(Product.builder()
                        .name("Monitor Curvo 27\"")
                        .description("Monitor curvo Full HD con 144Hz de tasa de refresco para una jugabilidad fluida.")
                        .price(219999.0)
                        .stock(10)
                        .imagen("https://ocelot.com.mx/wp-content/uploads/2025/05/FONDO_OSCURO-OM_C32-2.jpg")
                        .category(monitores)
                        .build());

                productRepository.save(Product.builder()
                        .name("Silla Gamer")
                        .description("Silla ergonómica reclinable para largas sesiones de juego con máximo confort.")
                        .price(149999.0)
                        .stock(8)
                        .imagen("https://ocelot.com.mx/wp-content/uploads/2023/07/FONDO-OSCURO-SAVAGE-RED-TELA-7.jpg")
                        .category(sillas)
                        .build());

                System.out.println("Datos de ejemplo cargados correctamente.");
            }
        };
    }
}