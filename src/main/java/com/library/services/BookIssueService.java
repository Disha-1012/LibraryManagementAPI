package com.library.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.entities.Book;
import com.library.entities.BookIssue;
import com.library.entities.Student;

import com.library.repositories.BookIssueRepository;
import com.library.repositories.BookRepository;
import com.library.repositories.StudentRepository;

import com.library.exceptions.ResourceNotFoundException;
import com.library.exceptions.BusinessRuleException;

@Service
public class BookIssueService {

    private final BookIssueRepository bookIssueRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    // Constructor injection
    public BookIssueService(
            BookIssueRepository bookIssueRepository,
            BookRepository bookRepository,
            StudentRepository studentRepository) {

        this.bookIssueRepository = bookIssueRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    // Issue a book to a student
    @Transactional
    public BookIssue issueBook(Long studentId, Long bookId) {

        // Check whether student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with ID: " + studentId));

        // Check whether book exists
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with ID: " + bookId));

        // Check whether copies are available
        if (book.getAvailableCopies() == null
                || book.getAvailableCopies() <= 0) {

            throw new BusinessRuleException(
                    "No copies available for this book");
        }

        // Check maximum three active issues per student
        long activeIssues =
                bookIssueRepository.countByStudent_StudentIdAndStatus(
                        studentId,
                        "ISSUED"
                );

        if (activeIssues >= 3) {

            throw new BusinessRuleException(
                    "A student can have a maximum of 3 active book issues");
        }

        // Create a new issue record
        BookIssue issue = new BookIssue();

        issue.setStudent(student);
        issue.setBook(book);

        // Set issue date
        LocalDate today = LocalDate.now();
        issue.setIssueDate(today);

        // Set due date (14 days after issue date)
        issue.setDueDate(today.plusDays(14));

        // Initially return date is null
        issue.setReturnDate(null);

        // Set status
        issue.setStatus("ISSUED");

        // Decrease available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        // Save updated book and issue record
        bookRepository.save(book);

        return bookIssueRepository.save(issue);
    }

    // Get all issue records
    public List<BookIssue> getAllIssues() {
        return bookIssueRepository.findAll();
    }

    // Get all issues of a particular student
    public List<BookIssue> getIssuesByStudent(Long studentId) {

        // Check whether student exists
        if (!studentRepository.existsById(studentId)) {

            throw new ResourceNotFoundException(
                    "Student not found with ID: " + studentId);
        }

        return bookIssueRepository.findByStudent_StudentId(studentId);
    }

    // Return a book
    @Transactional
    public BookIssue returnBook(Long issueId) {

        // Find the issue record
        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book issue not found with ID: " + issueId));

        // Prevent returning an already returned book
        if ("RETURNED".equalsIgnoreCase(issue.getStatus())) {

            throw new BusinessRuleException(
                    "This book has already been returned");
        }

        // Set return date and status
        issue.setReturnDate(LocalDate.now());
        issue.setStatus("RETURNED");

        // Increase available copies
        Book book = issue.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        // Save updated book and issue record
        bookRepository.save(book);

        return bookIssueRepository.save(issue);
    }

    // Get overdue books
    public List<BookIssue> getOverdueIssues() {

        return bookIssueRepository.findByStatusAndDueDateBefore(
                "ISSUED",
                LocalDate.now()
        );
    }
}
