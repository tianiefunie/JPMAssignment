# Show Booking Spring Boot Application


## Getting started
The project runs on java 8

To start the application, a vm arguement has to be included

```
start.enabled=true
```
Start the application by using the respective JAR file with the following commands.

```
java -jar -Dstart.enabled=true demo-0.0.1-SNAPSHOT.jar
```


## Commands
Commands for the application is as follows:

### Admin
- `Setup [show number] [number of rows] [number of seats per row] [cancellation window period]`
- `View [show number]`
- `Add [show number] [number of rows] [number of seats per row]`
    
###  Buyer
- `Availability [show number]`
- `Book [show number] [phone number] [seats desired separated by comma]`
- `Cancel [ticket number] [phone number]`
  
Please note that every command should begin with a capital letter as they are case sensitive. Additionally, ensure that there are spaces between each argument that is passed in. 

To switch between Admin and Buyer role, you can use `Leave` while in one role, and pick the other.

You can also exit the application with `exit`

## Assumptions:

1. Phone number does not have to be a valid one.

2. Seats desired are only having 2 characters max, where first character is an alphabet character and second one is an
   integer from 0-9.

3. Ticket numbers generated will always be unique using UUID

4. There will be no removal of rows/seats once the show has been configured at the start. You can only add but not
   remove.

5. Cancellation window can be in negative numbers, but it'll be equivalent to giving 0.

6. No extreme values will be given to break the system on purpose

## Run test cases
To run all test cases, navigate to the root directory of the Spring Boot folder and execute the command `mvn test` with any Maven version that is compatible with Java 8.
