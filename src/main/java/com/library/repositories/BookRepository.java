package com.library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.entities.Book;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find a book using ISBN
    Optional<Book> findByIsbn(String isbn);

    // Search books by category
    List<Book> findByCategoryIgnoreCase(String category);

}