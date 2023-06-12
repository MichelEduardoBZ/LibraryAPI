package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
@Getter
public class RentPenaltyDTO {

    @NotBlank
    @JsonProperty(value = "date_customer_returned")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private String dateCustomerReturned;

    private String nameClient;
    private String titleBook;
    String valuePenalty;

    public RentPenaltyDTO(String nameClient, String bookTitle, String valueReturned, String dateCustomerReturned) {
        this.nameClient = nameClient;
        this.titleBook = bookTitle;
        this.dateCustomerReturned = dateCustomerReturned;
        this.valuePenalty = String.valueOf(valueReturned);
    }

}
