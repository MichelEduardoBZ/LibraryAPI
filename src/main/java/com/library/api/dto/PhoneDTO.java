package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.api.entities.Phone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PhoneDTO {

    private Long id;

    @NotBlank
    @Size(min = 8, max = 12, message = "The number must contain between 8 and 12 digits")
    private String phone;

    @NotNull(message = "Must contain customer id")
    @JsonProperty(value = "client_id")
    private Long clientId;

    public PhoneDTO(Phone entity) {
        id = entity.getId();
        phone = entity.getPhone();
        clientId = entity.getClient().getId();
    }
}
