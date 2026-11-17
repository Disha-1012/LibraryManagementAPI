package com.library.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entities.Student;


public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find a student using email
    Optional<Student> findByEmail(String email);

}