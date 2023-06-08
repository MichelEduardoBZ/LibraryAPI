package com.library.api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "phone")
@Table(name = "tb_phone")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
