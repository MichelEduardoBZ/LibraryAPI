package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.api.entities.Book;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255, message = "Name must be between 1 and 80 digits")
    private String title;

    @NotBlank
    @Size(min = 3, max = 255, message = "Author must be between 3 and 80 digits\"")
    private String author;

    @NotBlank(message = "Year of publication is required")
    @JsonProperty("year_of_publication")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Required format: yyyy-MM-dd")
    private String yearOfPublication;

    @JsonProperty("price_day_rent")
    private Integer priceDayRent;

    public BookDTO(Book book) {
        id = book.getId();
        title = book.getTitle();
        author = book.getAuthor();
        yearOfPublication = String.valueOf(book.getYearOfPublication());
        priceDayRent = book.getPriceDayRent();
    }

    public BookDTO(BookDTO bookDTO) {
        id = bookDTO.id;
        title = bookDTO.title;
        author = bookDTO.author;
        yearOfPublication = String.valueOf(bookDTO.getYearOfPublication());
        priceDayRent = bookDTO.getPriceDayRent();
    }
}
