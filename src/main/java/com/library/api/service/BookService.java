package com.library.api.service;

import com.library.api.dto.BookDTO;
import com.library.api.entities.Book;
import com.library.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
        Book book = repository.getReferenceById(id);
        copyDtoToBook(bookDto, book);
        book = repository.save(book);
        return new BookDTO(book);
    }

    @Transactional(readOnly = true)
    public Page<BookDTO> searchBooks(Pageable pageable) {
        Page<Book> books = repository.findAll(pageable);
        return books.map(BookDTO::new);
    }

    @Transactional(readOnly = true)
    public BookDTO searchBookById(Long id) {
        Book book = repository.getReferenceById(id);
        return new BookDTO(book);
    }

    @Transactional
    public void deleteBookById(Long id) {
        repository.deleteById(id);
    }

    public void copyDtoToBook(BookDTO bookDto, Book book) {

        if (!bookDto.getName().isEmpty()) {
            book.setName(bookDto.getName());
        }

        if (!bookDto.getAuthor().isEmpty()) {
            book.setAuthor(bookDto.getAuthor());
        }

        if (!bookDto.getYearOfPublication().isEmpty()) {
            book.setYearOfPublication(LocalDate.parse(bookDto.getYearOfPublication()));
        }
    }
}
