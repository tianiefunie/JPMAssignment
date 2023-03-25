package com.example.demo.utils;

import com.example.demo.dto.Country;
import com.example.demo.dto.Show;
import com.example.demo.repo.CountryRepository;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CommandLineService implements CommandLineRunner {
    @Autowired
    private ApplicationContext context;

        @Override
        public void run(String... args) {
            String line = "";

            while(!line.equals("exit")) {
                System.out.println("Enter Command");
                Scanner scanner = new Scanner(System.in);
                line = scanner.nextLine();
                CountryRepository repo = context.getBean(CountryRepository.class);
                Country country = repo.findById(1);
                System.out.println(country.getName());
                Show show = new Show();
                show.getId();
            }
        }

}
