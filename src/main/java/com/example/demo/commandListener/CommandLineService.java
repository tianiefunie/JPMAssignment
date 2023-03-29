package com.example.demo.commandListener;

import com.example.demo.service.impl.AdminShowServiceImplementation;
import com.example.demo.service.impl.BuyerServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@ConditionalOnProperty(name = "start.enabled",havingValue="true")
public class CommandLineService implements CommandLineRunner {

    @Autowired
    AdminShowServiceImplementation adminShowServiceImplementation;
    @Autowired
    BuyerServiceImplementation buyerServiceImplementation;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (!line.equals("exit")) {
            System.out.println("Choose role");
            line = scanner.nextLine();
            if (line.startsWith("Admin")) {
                System.out.println("Admin role");
                while (!line.equals("Leave")) {
                    System.out.println("Enter command");
                    line = scanner.nextLine();
                    String[] commandArgs = line.split(" ");
                    if (commandArgs[0].equals("Setup") && commandArgs.length == 5) {
                        adminShowServiceImplementation.setupShow(commandArgs[1], commandArgs[2], commandArgs[3], commandArgs[4]);
                    } else if (commandArgs[0].equals("View") && commandArgs.length == 2) {
                        adminShowServiceImplementation.view(commandArgs[1]);
                    } else if (commandArgs[0].equals("Add") && commandArgs.length == 4) {
                        adminShowServiceImplementation.add(commandArgs[1], commandArgs[2], commandArgs[3]);
                    } else if (commandArgs[0].equals("Leave")) {
                        break;
                    } else {
                        System.out.println("Command incorrect or invalid arguments have been given. Please try another");
                    }
                }
            } else if (line.startsWith("Buyer")) {
                System.out.println("Buyer role");
                while (!line.equals("Leave role")) {
                    System.out.println("Enter command");
                    line = scanner.nextLine();
                    String[] commandArgs = line.split(" ");
                    if (commandArgs[0].equals("Availability") && commandArgs.length == 2) {
                        buyerServiceImplementation.availability(commandArgs[1]);
                    } else if (commandArgs[0].equals("Book") && commandArgs.length == 4) {
                        buyerServiceImplementation.book(commandArgs[1], commandArgs[2], commandArgs[3]);
                    } else if (commandArgs[0].equals("Cancel") && commandArgs.length == 3) {
                        buyerServiceImplementation.cancel(commandArgs[1], commandArgs[2]);
                    } else if (commandArgs[0].equals("Leave")) {
                        break;
                    } else {
                        System.out.println("Command incorrect or invalid arguments have been given. Please try another");
                    }
                }
            } else if (line.equals("exit")) {
                break;
            } else {
                System.out.println("Invalid role, please try again.");
            }


//                CountryRepository repo = context.getBean(CountryRepository.class);
//                Country country = repo.findById(1);
//                System.out.println(country.getName());
        }
    }

}
