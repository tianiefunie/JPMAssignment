package com.example.demo.service.impl;

import com.example.demo.dto.Buyer;
import com.example.demo.dto.SeatInformation;
import com.example.demo.dto.Show;
import com.example.demo.dto.Theatre;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdminShowServiceImplementationTest {
    @InjectMocks
    AdminShowServiceImplementation adminShowServiceImplementation;

    @Mock
    Theatre theatre;

    @Test
    public void testSetupShow() {
        // Test creating a new show
        Map<String, Show> shows = new HashMap<>();
        doReturn(shows).when(theatre).getShows();
        adminShowServiceImplementation.setupShow("1", "5", "5", "30");
        verify(theatre, times(1)).addShow(eq("1"), eq("5"), eq("5"), eq("30"));
    }

    @Test
    public void testSetupShowFailure() {
        // Test case for an existing show
        Map<String, Show> shows = new HashMap<>();
        Show show = new Show("1", "2", "3", "4");
        shows.put("1", show);
        doReturn(shows).when(theatre).getShows();
        adminShowServiceImplementation.setupShow("1", "5", "5", "30");
        verify(theatre, times(0)).addShow(anyString(), anyString(), anyString(), anyString());

        // Test case for invalid seat/row count
        adminShowServiceImplementation.setupShow("2", "27", "11", "30");
        verify(theatre, times(0)).addShow(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testViewShowExists() {
        //To capture output
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        //Set up show
        Map<String, Show> shows = new HashMap<>();
        Show show = new Show("1", "2", "2", "4");
        //To book seats
        SeatInformation[][] seatInformation = show.getSeatInformation();
        seatInformation[0][1].setAvailability(false);
        seatInformation[0][1].setBuyer(new Buyer("1", new HashMap<>()));
        seatInformation[0][1].setTicketNumber(UUID.randomUUID());
        seatInformation[0][1].setTimeBooked(123L);
        seatInformation[1][0].setAvailability(false);
        seatInformation[1][0].setBuyer(new Buyer("2", new HashMap<>()));
        seatInformation[1][0].setTicketNumber(UUID.randomUUID());
        seatInformation[1][0].setTimeBooked(123L);
        show.setSeatInformation(seatInformation);
        shows.put("1", show);
        doReturn(shows).when(theatre).getShows();
        adminShowServiceImplementation.view("1");

        // Expected output to assert
        StringBuilder expectedView = new StringBuilder();
        for (int i = 0; i < seatInformation.length; i++) {
            for (int j = 0; j < seatInformation[0].length; j++) {
                if (seatInformation[i][j].getAvailability().equals(true)) {
                    expectedView.append("Seat " + (char) (65 + i) + j + " is available for booking. \n");
                } else {
                    expectedView.append("Seat " + (char) (65 + i) + j + " has been booked by phone number " + seatInformation[i][j].getBuyer().getPhoneNumber() + " under ticket number " + seatInformation[i][j].getTicketNumber() + ".\n");
                }
            }
        }
        String expectedOutput = "Show number 1" + " details:\n" + expectedView;
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
    }

    @Test
    public void testViewShowDoesNotExist() {
        //To capture output
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));

        doReturn(new HashMap<>()).when(theatre).getShows();
        adminShowServiceImplementation.view("1");
        String expectedOutput = "Show number does not exists, please try another number";

        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());

    }

    @Test
    public void testAddSuccessfully() {
        Map<String, Show> shows = new HashMap<>();
        Show show = new Show("1", "2", "3", "4");
        shows.put("1", show);
        doReturn(shows).when(theatre).getShows();
        adminShowServiceImplementation.add("1", "2", "2");
        assertEquals(show.getSeatInformation().length, 4);
        assertEquals(show.getSeatInformation()[0].length, 5);
    }

    @Test
    public void testAddFailure() {
        //To capture output
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));

        //Test adding more than 26 rows & 10 seats
        Map<String, Show> shows = new HashMap<>();
        Show show = new Show("1", "26", "10", "4");
        shows.put("1", show);
        doReturn(shows).when(theatre).getShows();
        adminShowServiceImplementation.add("1", "2", "2");

        String expectedOutput = "Seats cannot be more than 10 and rows cannot be more than 26. Please try again";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
        actualOutput.reset();

        //Test adding to show that does not exist
        expectedOutput = "Show number does not exists, please try another number";

        adminShowServiceImplementation.add("2", "2", "2");
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
    }

}
