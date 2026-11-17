package com.library.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entities.BookIssue;
import com.library.services.BookIssueService;


@RestController
@RequestMapping("/api/issues")
public class BookIssueController {

    private final BookIssueService bookIssueService;

    // Constructor injection
    public BookIssueController(BookIssueService bookIssueService) {
        this.bookIssueService = bookIssueService;
    }

    // Issue a book
    @PostMapping("/{studentId}/{bookId}")
    public ResponseEntity<BookIssue> issueBook(
            @PathVariable Long studentId,
            @PathVariable Long bookId) {

        BookIssue issue =
            bookIssueService.issueBook(studentId, bookId);

        return new ResponseEntity<>(
            issue,
            HttpStatus.CREATED
        );
    }

    // Get all issue records
    @GetMapping
    public ResponseEntity<List<BookIssue>> getAllIssues() {

        return ResponseEntity.ok(
            bookIssueService.getAllIssues()
        );
    }

    // Get issues by student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<BookIssue>> getIssuesByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
            bookIssueService.getIssuesByStudent(studentId)
        );
    }

    // Return a book
    @PutMapping("/return/{issueId}")
    public ResponseEntity<BookIssue> returnBook(
            @PathVariable Long issueId) {

        BookIssue returnedIssue =
            bookIssueService.returnBook(issueId);

        return ResponseEntity.ok(returnedIssue);
    }

    // Get overdue books
    @GetMapping("/overdue")
    public ResponseEntity<List<BookIssue>> getOverdueIssues() {

        return ResponseEntity.ok(
            bookIssueService.getOverdueIssues()
        );
    }
}