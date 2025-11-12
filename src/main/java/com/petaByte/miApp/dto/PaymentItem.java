// PaymentItem.java
package com.petaByte.miApp.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentItem {
    private String title;
    private Integer quantity;
    private BigDecimal price; // más seguro para manejar dinero
}
