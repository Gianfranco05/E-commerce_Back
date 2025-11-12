package com.petaByte.miApp.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.resources.preference.Preference;
import com.petaByte.miApp.dto.PaymentItem;
import com.petaByte.miApp.dto.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Value("${mercado.pago.access.token}")
    private String accessToken; // Usar TEST-... para sandbox

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPreference(@RequestBody PaymentRequest request) {
        try {
            // Validar token
            if (accessToken == null || accessToken.isBlank()) {
                return ResponseEntity.internalServerError()
                        .body(Map.of("error", "Token de Mercado Pago no configurado"));
            }

            // Validar items
            if (request.getItems() == null || request.getItems().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "No se recibieron productos en la solicitud"));
            }

            for (PaymentItem item : request.getItems()) {
                if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "El item '" + item.getTitle() + "' tiene precio inválido"));
                }
                if (item.getQuantity() == null || item.getQuantity() <= 0) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "El item '" + item.getTitle() + "' tiene cantidad inválida"));
                }
            }

            // Configurar token
            MercadoPagoConfig.setAccessToken(accessToken);

            // Convertir PaymentItem a PreferenceItemRequest
            List<PreferenceItemRequest> items = request.getItems().stream()
                    .map(item -> PreferenceItemRequest.builder()
                            .title(item.getTitle())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getPrice())
                            .currencyId("ARS")
                            .build())
                    .collect(Collectors.toList());

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return ResponseEntity.ok(Map.of("init_point", preference.getInitPoint()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al crear la preferencia: " + e.getMessage()));
        }
    }

}
