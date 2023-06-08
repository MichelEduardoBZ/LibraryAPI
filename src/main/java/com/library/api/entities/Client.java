package com.library.api.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "client")
@Table(name = "tb_client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private LocalDate birthDate;

    @OneToMany(mappedBy = "client")
    private Set<Phone> phones = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Rent> rents;
}
