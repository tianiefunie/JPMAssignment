# Show Booking Spring Boot Application


## Getting started
The project runs on Java 8.

To start the application, a vm arguement needs to be included

```
start.enabled=true
```
To start the application using the respective JAR file, use the following command:
```
java -jar -Dstart.enabled=true ShowBookingApplication-0.0.1-SNAPSHOT
```


## Commands
Commands for the application is as follows:

### Admin
- Set up show:`Setup [show number] [number of rows] [number of seats per row] [cancellation window period]`
    - E.g. `Setup 1 2 3 4`
- View show by show number: `View [show number]`
    - E.g. `View 1`
- Add more rows/seats: `Add [show number] [number of rows] [number of seats per row]`
    - E.g. `Add 1 2 2`
- Change cancellation window period:`ChangeCancellationWindow [show number] [cancellation window period]`
    - E.g. `ChangeCancellationWindow 1 2`
    
###  Buyer
- Availability of show number: `Availability [show number]`
    - E.g. `Availability 1`
- Book show and seats under a phone number: `Book [show number] [phone number] [seats desired separated by comma]`
    - E.g. `Book 1 98764321 A0,A1,B1`
- Cancel booked show: `Cancel [ticket number] [phone number]`
    - E.g. `Cancel 0f4d4f7e-8f84-4e9e-9c15-7dc24f8b3201 987654321`
  
Please note that every command should begin with a capital letter as they are case sensitive. Additionally, ensure that there are spaces between each argument that is passed in. 

To switch between Admin and Buyer role, you can use `Leave` while in one role, and pick the other.

You can also exit the application with `exit`

## Assumptions

1. Phone number does not have to be a valid one.

2. Seats desired are only having 2 characters max, where first character is an alphabet character and second one is an
   integer from 0-9.

3. Ticket numbers generated will always be unique using UUID.

4. There will be no removal of rows/seats once the show has been configured at the start. You can only add but not
   remove.

5. Cancellation window can be in negative numbers, but it'll be equivalent to giving 0.

6. No extreme values will be given to break the system on purpose.

7. Shows set up will be running forever.

## Run test cases
To run all test cases, navigate to the root directory of the Spring Boot folder and execute the command `mvn test` with any Maven version that is compatible with Java 8.
