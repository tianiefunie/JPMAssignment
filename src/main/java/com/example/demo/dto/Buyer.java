package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Buyer {
    private String phoneNumber;
    private Map<UUID, Booking> bookingList;
}
