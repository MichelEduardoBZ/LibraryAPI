package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.text.DecimalFormat;
import java.time.LocalDate;

@Getter
public class RentPenaltyDTO {

    @NotBlank
    @JsonProperty(value = "date_customer_returned")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private String dateCustomerReturned;

    private String nameClient;

    private String titleBook;

    String valuePenalty;

    public RentPenaltyDTO(String nameClient, String bookTitle, Double valuePenalty, LocalDate dateCustomerReturned) {
        this.nameClient = nameClient;
        this.titleBook = bookTitle;
        this.valuePenalty = setCash(valuePenalty);
        this.dateCustomerReturned = String.valueOf(dateCustomerReturned);
    }

    public String setCash(Double valuePenalty) {
        DecimalFormat df = new DecimalFormat("###,##0.00");
        return df.format(valuePenalty);
    }


}
