package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SeatInformation {
    private Boolean availability;
    private Long timeBooked;
    private Buyer buyer;
    private UUID ticketNumber;

}