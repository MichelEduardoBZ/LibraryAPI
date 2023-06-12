package com.library.api;

import com.library.api.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

    @Autowired
    private RentService service;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

}
