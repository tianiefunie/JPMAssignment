package com.example.demo.service;

public interface BuyerService {
    public void availability(String showNumber);
    public void book(String showNumber, String phoneNumber, String seats);
    public void cancel(String ticketNumber, String phoneNumber);
}
