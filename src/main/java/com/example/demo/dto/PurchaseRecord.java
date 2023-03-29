package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@AllArgsConstructor
public class PurchaseRecord {
    private Map<String,Buyer> buyers;

}
