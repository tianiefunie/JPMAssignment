package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Booking {
    private UUID ticketNumber;
    private String showNumber;
    private List<String> seats;
}