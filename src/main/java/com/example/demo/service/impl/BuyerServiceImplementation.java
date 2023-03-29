package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BuyerServiceImplementation implements BuyerService {
    @Autowired
    Theatre theatre;
    @Autowired
    PurchaseRecord purchaseRecord;
    @Override
    public void availability(String showNumber) {
        if (theatre.getShows().containsKey(showNumber)) {
            ArrayList<String> seatsAvailable = new ArrayList<String>();
            SeatInformation[][] seatInformation = theatre.getShows().get(showNumber).getSeatInformation();
            for(int i = 0; i< seatInformation.length;i++) {
                for(int j = 0; j<seatInformation[0].length;j++ ) {
                    if (seatInformation[i][j].getAvailability()) {
                        seatsAvailable.add((char) (65+i) + String.valueOf(j));
                    }
                }
            }
            System.out.println(seatsAvailable);
        } else {
            System.out.println("Show number does not exists, please try another number");
        }
    }

    @Override
    public void book(String showNumber, String phoneNumber, String seats) {
        List<String> seatsList = Arrays.asList(seats.split(","));
        if(theatre.getShows().containsKey(showNumber)) {
            if(checkSeatsAvailability(showNumber,seatsList)) {
                if(!purchaseRecord.getBuyers().containsKey(phoneNumber)) {
                    Map<UUID,Booking> bookingList = new HashMap<>();
                    UUID ticketNumber = UUID.randomUUID();
                    Booking booking = new Booking(ticketNumber, showNumber, seatsList);
                    bookingList.put(ticketNumber,booking);
                    Buyer buyer = new Buyer(phoneNumber, bookingList);
                    purchaseRecord.getBuyers().put(phoneNumber,buyer);
                    bookingSeats(showNumber, phoneNumber, seatsList, buyer, ticketNumber);
                } else {
                    if(checkValidityOfBooking(showNumber, phoneNumber)) {
                        Buyer buyer= purchaseRecord.getBuyers().get(phoneNumber);
                        UUID ticketNumber = UUID.randomUUID();
                        Booking booking = new Booking(ticketNumber,showNumber,seatsList);
                        Map<UUID,Booking> bookingList = buyer.getBookingList();
                        bookingList.put(ticketNumber,booking);
                        bookingSeats(showNumber, phoneNumber, seatsList, buyer, ticketNumber);
                    } else {
                        System.out.println("Current phone number has booked this show before, please try another show.");
                    }
                }
            } else {
                System.out.println("1 or more seats are unavailable for booking. Please try again");
            }
        } else {
            System.out.println("Show number does not exists, please try another number.");
        }
    }

    @Override
    public void cancel(String ticketNumber, String phoneNumber) {
        if (purchaseRecord.getBuyers().containsKey(phoneNumber)) {
            if (purchaseRecord.getBuyers().get(phoneNumber).getBookingList().containsKey(UUID.fromString(ticketNumber))) {
                Booking booking = purchaseRecord.getBuyers().get(phoneNumber).getBookingList().get(UUID.fromString(ticketNumber));
                if (checkValidityOfCancellation(booking)) {
                    SeatInformation[][] seatInformation = theatre.getShows().get(booking.getShowNumber()).getSeatInformation();
                    for(int i=0; i<booking.getSeats().size();i++) {
                        Integer rowNumber = booking.getSeats().get(i).charAt(0)-65;
                        Integer seatNumber = Character.getNumericValue(booking.getSeats().get(i).charAt(1));
                        seatInformation[rowNumber][seatNumber].setAvailability(true);
                        seatInformation[rowNumber][seatNumber].setTimeBooked(null);
                        seatInformation[rowNumber][seatNumber].setBuyer(null);
                        seatInformation[rowNumber][seatNumber].setTicketNumber(null);
                    }
                    purchaseRecord.getBuyers().get(phoneNumber).getBookingList().remove(UUID.fromString(ticketNumber));
                    System.out.println("Ticket number " + ticketNumber + " has been successfully cancelled.");
                } else {
                    System.out.println("Sorry but the ticket's cancellation window period over.");
                }
            } else {
                System.out.println("Ticket Number does not exist under this phone number, please try again.");
            }
        } else {
            System.out.println("Phone number does not exist, please try again.");
        }
    }

    private boolean checkValidityOfBooking(String showNumber, String phoneNumber) {
        Buyer buyer = purchaseRecord.getBuyers().get(phoneNumber);
        for(Map.Entry<UUID,Booking> entry : buyer.getBookingList().entrySet()) {
            if (buyer.getBookingList().get(entry.getKey()).getShowNumber().equals(showNumber)) return false;
        }
        return true;
    }
    private boolean checkSeatsAvailability(String showNumber,List<String> seatsList) {
        SeatInformation[][] seatInformation = theatre.getShows().get(showNumber).getSeatInformation();

        for(int i=0;i<seatsList.size();i++) {
            if (Character.isAlphabetic(seatsList.get(i).charAt(0)) && Character.isDigit(seatsList.get(i).charAt(1))&&seatsList.get(i).length()<3) {
                Integer rowNumber = seatsList.get(i).charAt(0)-65;
                Integer seatNumber = Character.getNumericValue(seatsList.get(i).charAt(1));
                if(rowNumber >= seatInformation.length || seatNumber >= seatInformation[0].length) {
                    return false;
                } else if(seatInformation[rowNumber][seatNumber].getAvailability().equals(false)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
    private void bookingSeats(String showNumber,String phoneNumber,List<String > seatsList, Buyer buyer, UUID ticketNumber) {
        SeatInformation[][] seatInformation = theatre.getShows().get(showNumber).getSeatInformation();

        for(int i=0; i<seatsList.size();i++) {
            Integer rowNumber = seatsList.get(i).charAt(0)-65;
            Integer seatNumber = Character.getNumericValue(seatsList.get(i).charAt(1));
            seatInformation[rowNumber][seatNumber].setAvailability(false);
            seatInformation[rowNumber][seatNumber].setTimeBooked(System.currentTimeMillis());
            seatInformation[rowNumber][seatNumber].setBuyer(buyer);
            seatInformation[rowNumber][seatNumber].setTicketNumber(ticketNumber);
        }

        System.out.println("You have successfully booked seats " + seatsList + " for show " + showNumber +  " under the follow phone number " + phoneNumber+ ". Your ticket number is " + ticketNumber + ".");
    }

    private boolean checkValidityOfCancellation(Booking booking) {
        Integer rowNumber = booking.getSeats().get(0).charAt(0)-65;
        Integer seatNumber = Character.getNumericValue(booking.getSeats().get(0).charAt(1));
        long elapsedMinutes = (System.currentTimeMillis() - theatre.getShows().get(booking.getShowNumber()).getSeatInformation()[rowNumber][seatNumber].getTimeBooked()) / 60000;
        if (elapsedMinutes >= Long.parseLong(theatre.getShows().get(booking.getShowNumber()).getCancellationWindowInMinutes())) {
            return false;
        } else {
            return true;
        }
    }

}
