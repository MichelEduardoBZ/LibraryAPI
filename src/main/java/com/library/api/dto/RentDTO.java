package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.api.entities.Rent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentDTO {

    private Long id;

    @NotBlank
    @JsonProperty(value = "rent_date")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private String rentDate;

    @NotBlank
    @JsonProperty(value = "devolution_date")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private String devolutionDate;

    @NotNull(message = "Rent date is required")
    @JsonProperty(value = "client_id")
    private Long clientId;

    @NotNull(message = "Devolution date is required")
    @JsonProperty(value = "book_id")
    private Long bookId;

    public RentDTO(Rent entity){
        id = entity.getId();
        rentDate = String.valueOf(entity.getRentDate());
        devolutionDate = String.valueOf(entity.getDevolutionDate());
        clientId = entity.getClient().getId();
        bookId = entity.getBook().getId();
    }

}
