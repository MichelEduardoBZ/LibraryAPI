package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.api.constants.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RentPaymentDTO {

    @JsonProperty(value = "rent_id")
    private Long rentId;

    @JsonProperty(value = "client_id")
    private Long clientId;

    @JsonProperty(value = "book_id")
    private Long bookId;

    @NotBlank
    @JsonProperty(value = "book_return_day")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private String bookReturnDay;

    @NotBlank
    private String cash;

    private PaymentStatus paymentStatus;

    public RentPaymentDTO(Long rentId, Long clientId, Long bookId, LocalDate bookReturnDay, Double cash, PaymentStatus paymentStatus) {
        this.rentId = rentId;
        this.clientId = clientId;
        this.bookId = bookId;
        this.bookReturnDay = String.valueOf(bookReturnDay);
        this.cash = String.format("%.2f", cash);
        this.paymentStatus = paymentStatus;
    }

}
