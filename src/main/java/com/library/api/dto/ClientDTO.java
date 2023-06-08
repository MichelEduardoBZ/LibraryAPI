package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.api.entities.Client;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ClientDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 80, message = "Name must be between 3 and 80 digits")
    private String name;

    @NotBlank
    @Size(min = 11, max = 14, message = "CPF must be between 11 and 14 digits")
    private String cpf;

    @NotBlank
    @Email(message = "Required format: name@domain")
    private String email;

    @NotBlank(message = "Birth date is required")
    @JsonProperty("birth_date")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private String birthDate;

    public ClientDTO(Client entity) {
        id = entity.getId();
        name = entity.getName();
        cpf = entity.getCpf();
        email = entity.getEmail();
        birthDate = String.valueOf(entity.getBirthDate());
    }


}
