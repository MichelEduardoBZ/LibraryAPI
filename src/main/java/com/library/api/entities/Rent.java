package com.library.api.entities;

import com.library.api.constants.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "rent")
@Table(name = "tb_rent")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate rentDate;
    private LocalDate devolutionDate;
    private Integer paymentStatus;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Rent() {
    }

    public Rent(Long id, LocalDate rentDate, LocalDate devolutionDate, PaymentStatus paymentStatus, Client client, Book book) {
        this.id = id;
        this.rentDate = rentDate;
        this.devolutionDate = devolutionDate;
        setPaymentStatus(paymentStatus);
        this.client = client;
        this.book = book;
    }

    public PaymentStatus getPaymentStatus() {
        return PaymentStatus.valueOf(paymentStatus);
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        if (paymentStatus != null) {
            this.paymentStatus = paymentStatus.getCode();
        }
    }
}
