package com.library.api.repository;

import com.library.api.dto.BookDTO;
import com.library.api.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT new com.library.api.dto.BookDTO(obj.book) " +
            "FROM rent obj " +
            "WHERE obj.book.author = :author")
    Page<BookDTO> searchBookByAuthor(String author, Pageable pageable);
}
