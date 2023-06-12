package com.library.api.service;

import com.library.api.dto.BookDTO;
import com.library.api.entities.Book;
import com.library.api.repository.BookRepository;
import com.library.api.service.exceptions.DatabaseException;
import com.library.api.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    @Transactional
    public BookDTO insertBook(BookDTO bookDto) {
        Book book = new Book();
        copyDtoToBook(bookDto, book);
        repository.save(book);
        return new BookDTO(book);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDto) {
        try {
            Book book = repository.getReferenceById(id);
            copyDtoToBook(bookDto, book);
            book = repository.save(book);
            return new BookDTO(book);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Book does not exist");
        }
    }

    @Transactional(readOnly = true)
    public Page<BookDTO> searchBooks(Pageable pageable) {
        Page<Book> books = repository.findAll(pageable);
        return books.map(BookDTO::new);
    }

    @Transactional(readOnly = true)
    public BookDTO searchBookById(Long id) {
        Book book = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book does not exist"));
        return new BookDTO(book);
    }

    @Transactional(readOnly = true)
    public Page<BookDTO> searchBookByAuthor(String author, Pageable pageable) {
        Page<BookDTO> book = repository.searchBookByAuthor(author, pageable);
        return book.map(BookDTO::new);
    }

    @Transactional
    public void deleteBookById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Book does not exist");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");
        }
    }

    public void copyDtoToBook(BookDTO bookDto, Book book) {

        LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        LocalDate yearOfPublication = LocalDate.parse(bookDto.getYearOfPublication());

        if (!bookDto.getTitle().isEmpty()) {
            book.setTitle(bookDto.getTitle());
        }

        if (!bookDto.getAuthor().isEmpty()) {
            book.setAuthor(bookDto.getAuthor());
        }

        if (!bookDto.getYearOfPublication().isEmpty()) {
            if (yearOfPublication.isAfter(today)) {
                throw new ResourceNotFoundException("Date cannot be greater than the current date");
            }
            book.setYearOfPublication(LocalDate.parse(bookDto.getYearOfPublication()));
        }

        if (bookDto.getPriceDayRent() != 0) {
            book.setPriceDayRent(bookDto.getPriceDayRent());
        }
    }
}
