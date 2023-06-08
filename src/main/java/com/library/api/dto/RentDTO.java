package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.api.entities.Rent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentDTO {

    private Long id;

    @JsonProperty(value = "rent_date")
    private String rentDate;

    @JsonProperty(value = "devolution_date")
    private String devolutionDate;

    @NotBlank(message = "Rent date is required")
    @JsonProperty(value = "client_id")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private Long clientId;

    @NotBlank(message = "Devolution date is required")
    @JsonProperty(value = "book_id")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private Long bookId;

    public RentDTO(Rent entity){
        id = entity.getId();
        rentDate = String.valueOf(entity.getRentDate());
        devolutionDate = String.valueOf(entity.getDevolutionDate());
        clientId = entity.getClient().getId();
        bookId = entity.getBook().getId();
    }

}
