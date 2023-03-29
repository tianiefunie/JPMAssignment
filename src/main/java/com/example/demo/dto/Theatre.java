package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@AllArgsConstructor
public class Theatre {
    private Map<String,Show> shows;

    public void addShow(String showNumber, String numRows, String numSeatsPerRow, String cancellationWindowMinutes) {
        Show show = new Show(showNumber,numRows,numSeatsPerRow,cancellationWindowMinutes);
        this.shows.put(showNumber,show);
    }

}
