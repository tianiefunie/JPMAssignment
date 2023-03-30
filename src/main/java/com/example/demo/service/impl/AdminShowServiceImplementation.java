package com.example.demo.service.impl;

import com.example.demo.dto.SeatInformation;
import com.example.demo.dto.Theatre;
import com.example.demo.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminShowServiceImplementation implements ShowService {

    @Autowired
    Theatre theatre;

    @Override
    public void setupShow(String showNumber, String numRows, String numSeatsPerRow, String cancellationWindowMinutes) {
        if (!theatre.getShows().containsKey(showNumber)) {
            if (Integer.parseInt(numRows) > 26 || Integer.parseInt(numSeatsPerRow) > 10) {
                System.out.println("Seats cannot be more than 10 and rows cannot be more than 26. Please try again");
            } else {
                theatre.addShow(showNumber, numRows, numSeatsPerRow, cancellationWindowMinutes);
            }
        } else {
            System.out.println("Show number exists, please try another number");
        }
    }

    @Override
    public void view(String showNumber) {
        if (theatre.getShows().containsKey(showNumber)) {
            StringBuilder view = new StringBuilder();
            SeatInformation[][] seatInformation = theatre.getShows().get(showNumber).getSeatInformation();
            for (int i = 0; i < seatInformation.length; i++) {
                for (int j = 0; j < seatInformation[0].length; j++) {
                    if (seatInformation[i][j].getAvailability().equals(true)) {
                        view.append("Seat " + (char) (65 + i) + j + " is available for booking. \n");
                    } else {
                        view.append("Seat " + (char) (65 + i) + j + " has been booked by phone number " + seatInformation[i][j].getBuyer().getPhoneNumber() + " under ticket number " + seatInformation[i][j].getTicketNumber() + ".\n");
                    }
                }
            }
            System.out.println("Show number " + showNumber + " details:" + "\n" + view);
        } else {
            System.out.println("Show number does not exists, please try another number");
        }
    }

    @Override
    public void add(String showNumber, String numRows, String numSeatsPerRow) {
        if (theatre.getShows().containsKey(showNumber)) {
            SeatInformation[][] oldSeatInformation = theatre.getShows().get(showNumber).getSeatInformation();
            int currentRow = theatre.getShows().get(showNumber).getSeatInformation().length;
            int currentSeatsPerRow = theatre.getShows().get(showNumber).getSeatInformation()[0].length;
            if (currentRow + Integer.parseInt(numRows) > 26 || currentSeatsPerRow + Integer.parseInt(numSeatsPerRow) > 10) {
                System.out.println("Seats cannot be more than 10 and rows cannot be more than 26. Please try again");
            } else {
                SeatInformation[][] newSeatInformation = new SeatInformation[currentRow + Integer.parseInt(numRows)][currentSeatsPerRow + Integer.parseInt(numSeatsPerRow)];
                for (int i = 0; i < newSeatInformation.length; i++) {
                    for (int j = 0; j < newSeatInformation[0].length; j++) {
                        if (i < currentRow && j < currentSeatsPerRow) {
                            newSeatInformation[i][j] = oldSeatInformation[i][j];
                        } else {
                            newSeatInformation[i][j] = new SeatInformation(true, null, null, null);
                        }
                    }
                }
                theatre.getShows().get(showNumber).setSeatInformation(newSeatInformation);
            }

        } else {
            System.out.println("Show number does not exists, please try another number");
        }
    }

    @Override
    public void changeCancellationWindow(String showNumber, String cancellationWindow) {
        if (theatre.getShows().containsKey(showNumber)) {
            theatre.getShows().get(showNumber).setCancellationWindowInMinutes(cancellationWindow);
        } else {
            System.out.println("Show number does not exists, please try another number");
        }
    }

}
