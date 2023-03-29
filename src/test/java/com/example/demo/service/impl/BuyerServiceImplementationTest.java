package com.example.demo.service.impl;

import com.example.demo.dto.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.print.Book;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest
public class BuyerServiceImplementationTest {
    @InjectMocks
    BuyerServiceImplementation buyerServiceImplementation;

    @Mock
    Theatre theatre;

    @Mock
    PurchaseRecord purchaseRecord;

    @Test
    public void testAvailability() {
        //To capture output
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));

        Map<String, Show> shows = new HashMap<>();
        Show show = new Show("1", "2", "3", "4");
        shows.put("1", show);
        doReturn(shows).when(theatre).getShows();

        buyerServiceImplementation.availability("1");

        //To get expected output
        SeatInformation[][] seatInformation = show.getSeatInformation();
        ArrayList<String> expectedSeatsAvailable = new ArrayList<String>();
        for (int i = 0; i < seatInformation.length; i++) {
            for (int j = 0; j < seatInformation[0].length; j++) {
                if (seatInformation[i][j].getAvailability()) {
                    expectedSeatsAvailable.add((char) (65 + i) + String.valueOf(j));
                }
            }
        }
        assertEquals(expectedSeatsAvailable.toString().trim(), actualOutput.toString().trim());
    }

    @Test
    public void testAvailabilityWrongShow() {
        //To capture output
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        doReturn(new HashMap<>()).when(theatre).getShows();
        buyerServiceImplementation.availability("1");
        String expectedOutput = "Show number does not exists, please try another number";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
    }

    @Test
    public void testBookSuccessful() {
        //Buyer buying first time ever
        Show show1 = new Show("1", "2", "3", "4");
        Show show2 = new Show("2", "3", "4", "5");
        Map<String, Show> shows = new HashMap<>();
        shows.put("1", show1);
        shows.put("2", show2);
        doReturn(shows).when(theatre).getShows();
        Map<String, Buyer> buyers = new HashMap<>();
        doReturn(buyers).when(purchaseRecord).getBuyers();
        buyerServiceImplementation.book("1", "1", "A0");
        assertEquals(buyers.get("1").getBookingList().size(), 1);
        assertEquals(show1.getSeatInformation()[0][0].getAvailability(), false);

        //Buyer purchasing 2nd time different show
        buyerServiceImplementation.book("2", "1", "A0");
        assertEquals(buyers.get("1").getBookingList().size(), 2);
        assertEquals(show2.getSeatInformation()[0][0].getAvailability(), false);

    }

    @Test
    public void testBookUnsuccessfully() {
        //To capture output
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));

        //Buyer chose wrong show number
        Show show = new Show("1", "2", "3", "4");
        Map<String, Show> shows = new HashMap<>();
        shows.put("1", show);
        doReturn(shows).when(theatre).getShows();
        Map<String, Buyer> buyers = new HashMap<>();
        doReturn(buyers).when(purchaseRecord).getBuyers();
        buyerServiceImplementation.book("2", "1", "A0");
        String expectedOutput = "Show number does not exists, please try another number.";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
        actualOutput.reset();

        //Buyer chose unavailable seats that are out of range of show
        buyerServiceImplementation.book("1", "1", "-A25");
        expectedOutput = "1 or more seats are unavailable for booking. Please try again";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
        actualOutput.reset();

        //Buyer chose unavailable seats that are not opened in the show
        buyerServiceImplementation.book("1", "1", "D6");
        expectedOutput = "1 or more seats are unavailable for booking. Please try again";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
        actualOutput.reset();

        //Buyer chose unavailable seats that has already been booked
        SeatInformation[][] seatInformation = show.getSeatInformation();
        seatInformation[0][0].setAvailability(false);
        buyerServiceImplementation.book("1", "1", "A0");
        expectedOutput = "1 or more seats are unavailable for booking. Please try again";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
        actualOutput.reset();

        //Buyer has booked the same show before
        //Setting up buyer booked show number 1 before
        UUID ticketNumber = UUID.randomUUID();
        Booking booking = new Booking(ticketNumber, "1", new ArrayList<>());
        Map<UUID, Booking> bookings = new HashMap<>();
        bookings.put(ticketNumber, booking);
        String phoneNumber = "321";
        Buyer buyer = new Buyer(phoneNumber, bookings);
        buyers.put(phoneNumber, buyer);
        doReturn(buyers).when(purchaseRecord).getBuyers();
        buyerServiceImplementation.book("1", phoneNumber, "B2");
        expectedOutput = "Current phone number has booked this show before, please try another show.";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
    }

    @Test
    public void testCancelSuccessful() {
        // Setting up buyer already booked show number 1 seat A0
        UUID ticketNumber = UUID.randomUUID();
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A0");
        Booking booking = new Booking(ticketNumber, "1", bookedSeats);
        Map<UUID, Booking> bookings = new HashMap<>();
        bookings.put(ticketNumber, booking);
        String phoneNumber = "321";
        Buyer buyer = new Buyer(phoneNumber, bookings);
        Map<String, Buyer> buyers = new HashMap<>();
        buyers.put(phoneNumber, buyer);
        doReturn(buyers).when(purchaseRecord).getBuyers();
        // Setting up show being booked by buyer above
        Show show = new Show("1", "2", "3", "4");
        SeatInformation[][] seatInformation = show.getSeatInformation();
        seatInformation[0][0].setBuyer(buyer);
        seatInformation[0][0].setTimeBooked(System.currentTimeMillis());
        seatInformation[0][0].setTicketNumber(ticketNumber);
        seatInformation[0][0].setAvailability(false);
        Map<String, Show> shows = new HashMap<>();
        shows.put("1", show);
        doReturn(shows).when(theatre).getShows();

        assertEquals(buyers.get(phoneNumber).getBookingList().size(), 1);
        assertEquals(show.getSeatInformation()[0][0].getAvailability(), false);
        buyerServiceImplementation.cancel(ticketNumber.toString(), phoneNumber);
        assertEquals(show.getSeatInformation()[0][0].getAvailability(), true);
        assertEquals(buyers.get(phoneNumber).getBookingList().size(), 0);
    }

    @Test
    public void testCancelUnsuccessful() {
        // Setting up buyer already booked show number 1 seat A0
        UUID ticketNumber = UUID.randomUUID();
        List<String> bookedSeats = new ArrayList<>();
        bookedSeats.add("A0");
        Booking booking = new Booking(ticketNumber, "1", bookedSeats);
        Map<UUID, Booking> bookings = new HashMap<>();
        bookings.put(ticketNumber, booking);
        String phoneNumber = "321";
        Buyer buyer = new Buyer(phoneNumber, bookings);
        Map<String, Buyer> buyers = new HashMap<>();
        buyers.put(phoneNumber, buyer);
        doReturn(buyers).when(purchaseRecord).getBuyers();
        // Setting up show being booked by buyer above but show has 0 minutes cancellation window
        Show show = new Show("1", "2", "3", "0");
        SeatInformation[][] seatInformation = show.getSeatInformation();
        seatInformation[0][0].setBuyer(buyer);
        seatInformation[0][0].setTimeBooked(System.currentTimeMillis());
        seatInformation[0][0].setTicketNumber(ticketNumber);
        seatInformation[0][0].setAvailability(false);
        Map<String, Show> shows = new HashMap<>();
        shows.put("1", show);
        doReturn(shows).when(theatre).getShows();

        assertEquals(buyers.get(phoneNumber).getBookingList().size(), 1);
        assertEquals(show.getSeatInformation()[0][0].getAvailability(), false);
        buyerServiceImplementation.cancel(ticketNumber.toString(), phoneNumber);
        assertEquals(buyers.get(phoneNumber).getBookingList().size(), 1);
        assertEquals(show.getSeatInformation()[0][0].getAvailability(), false);

        //Buyer entering wrong ticket number
        //To capture output
        ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actualOutput));
        buyerServiceImplementation.cancel(UUID.randomUUID().toString(), phoneNumber);
        String expectedOutput = "Ticket Number does not exist under this phone number, please try again.";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
        actualOutput.reset();

        //Buyer entered wrong phone number
        buyerServiceImplementation.cancel(ticketNumber.toString(), "999");
        expectedOutput = "Phone number does not exist, please try again.";
        assertEquals(expectedOutput.trim(), actualOutput.toString().trim());
    }
}
