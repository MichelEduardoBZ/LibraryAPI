package com.library.api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity(name = "book")
@Table(name = "tb_book")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
    private LocalDate yearOfPublication;
}

