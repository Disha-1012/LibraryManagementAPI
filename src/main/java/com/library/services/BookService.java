package com.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.Book;
import com.library.repositories.BookRepository;
import com.library.exceptions.ResourceNotFoundException;
import com.library.exceptions.DuplicateResourceException;

@Service
public class BookService {

    private final BookRepository bookRepository;

    // Constructor injection
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Add a new book
    public Book addBook(Book book) {

        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new DuplicateResourceException(
                    "Book with ISBN " + book.getIsbn() + " already exists");
        }

        return bookRepository.save(book);
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID
    public Book getBookById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with ID: " + id));
    }

    // Update book details
    public Book updateBook(Long id, Book updatedBook) {

        // Find existing book
        Book existingBook = getBookById(id);

        // Check duplicate ISBN
        bookRepository.findByIsbn(updatedBook.getIsbn())
                .ifPresent(book -> {

                    if (!book.getBookId().equals(id)) {
                        throw new DuplicateResourceException(
                                "A book with ISBN "
                                        + updatedBook.getIsbn()
                                        + " already exists");
                    }
                });

        // Update book details
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setTotalCopies(updatedBook.getTotalCopies());
        existingBook.setAvailableCopies(updatedBook.getAvailableCopies());

        return bookRepository.save(existingBook);
    }

    // Delete book
    public void deleteBook(Long id) {

        Book book = getBookById(id);

        bookRepository.delete(book);
    }

    // Search books by category
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryIgnoreCase(category);
    }
}
