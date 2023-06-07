package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.api.entities.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ClientDto {

    private Long id;

    private String name;
    private String email;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    public ClientDto(Client entity){
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        birthDate = entity.getBirthDate();
    }

}
