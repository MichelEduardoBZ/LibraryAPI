package com.library.api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity(name = "book")
@Table(name = "tb_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private LocalDate yearOfPublication;
    private Integer priceDayRent;

    @OneToMany(mappedBy = "book")
    private Set<Rent> rents;
}

