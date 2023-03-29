package com.example.demo.dto;

import lombok.Data;

@Data
public class Show {

    private String showNumber;
    private String numRows;
    private String numSeatsPerRow;
    private String cancellationWindowInMinutes;
    private SeatInformation[][] seatInformation;

    public Show(String showNumber, String numRows, String numSeatsPerRow, String cancellationWindowInMinutes) {
        this.showNumber=showNumber;
        this.numRows=numRows;
        this.numSeatsPerRow=numSeatsPerRow;
        this.cancellationWindowInMinutes=cancellationWindowInMinutes;
        this.seatInformation = new SeatInformation[Integer.parseInt(numRows)][Integer.parseInt(numSeatsPerRow)];
        for(int i=0; i<Integer.parseInt(numRows);i++){
            for(int j=0;j<Integer.parseInt(numSeatsPerRow);j++){
                this.seatInformation[i][j] = new SeatInformation(true,null,null,null);
            }
        }
    }
}
