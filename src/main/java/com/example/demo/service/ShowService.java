package com.example.demo.service;

public interface ShowService {
    public void setupShow(String showNumber, String numRows, String numSeatsPerRow, String cancellationWindowMinutes);
    public void view(String showNumber);
    public void add(String showNumber,String numRows, String numSeatsPerRow);
}
