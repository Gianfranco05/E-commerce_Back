// PaymentRequest.java
package com.petaByte.miApp.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    @NonNull
    private List<PaymentItem> items = new ArrayList<>(); // inicializada para evitar null
}
