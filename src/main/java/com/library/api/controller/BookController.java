package com.library.api.controller;

import com.library.api.dto.BookDTO;
import com.library.api.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    private BookService service;

    @PostMapping
    public ResponseEntity<BookDTO> insertBook(@RequestBody @Valid BookDTO bookDto) {
        bookDto = service.insertBook(bookDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(bookDto.getId()).toUri();
        return ResponseEntity.created(uri).body(bookDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody @Valid BookDTO bookDto) {
        bookDto = service.updateBook(id, bookDto);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> searchBooks(Pageable pageable) {
        Page<BookDTO> bookDtos = service.searchBooks(pageable);
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BookDTO> searchBookById(@PathVariable Long id) {
        BookDTO bookDto = service.searchBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping(value = "/booksDate")
    public ResponseEntity<Page<BookDTO>> searcBooksPublication(@PageableDefault(sort = {"yearOfPublication"}) Pageable pageable) {
        Page<BookDTO> bookDtos = service.searchBooks(pageable);
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping(value = "/author")
    public ResponseEntity<Page<BookDTO>> searcBooksByAuthor(@RequestParam(name = "author", defaultValue = "") String author, Pageable pageable) {
        Page<BookDTO> bookDtos = service.searchBookByAuthor(author, pageable);
        return ResponseEntity.ok(bookDtos);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        service.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
