package com.petaByte.miApp.payload.request;

import  lombok.Data;
@Data

public class AddItemRequest {
    private Long productId;
    private int quantity;
}
