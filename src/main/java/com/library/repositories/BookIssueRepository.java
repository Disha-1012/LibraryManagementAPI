package com.library.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entities.BookIssue;

public interface BookIssueRepository
        extends JpaRepository<BookIssue, Long> {

    // Find all issues belonging to a particular student
    List<BookIssue> findByStudent_StudentId(Long studentId);

    // Count active issues of a particular student
    long countByStudent_StudentIdAndStatus(
        Long studentId,
        String status
    );

    // Find overdue issues
    List<BookIssue> findByStatusAndDueDateBefore(
        String status,
        LocalDate date
    );
}