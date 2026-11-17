package com.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.Student;
import com.library.repositories.StudentRepository;
import com.library.exceptions.ResourceNotFoundException;
import com.library.exceptions.DuplicateResourceException;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor injection
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Register a new student
    public Student addStudent(Student student) {

        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new DuplicateResourceException(
                    "Student with email " + student.getEmail()
                            + " already exists");
        }

        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with ID: " + id));
    }

    // Update student
    public Student updateStudent(Long id, Student student) {

        // Check whether student exists
        Student existingStudent = getStudentById(id);

        // Check whether the new email belongs to another student
        studentRepository.findByEmail(student.getEmail())
        .ifPresent(updatedStudent -> {

            if (!updatedStudent.getStudentId().equals(id)) {
                throw new DuplicateResourceException(
                        "Another student with email "
                                + student.getEmail()
                                + " already exists");
            }
        });
        
        // Update student details
        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDepartment(student.getDepartment());
        existingStudent.setPhoneNumber(student.getPhoneNumber());

        return studentRepository.save(existingStudent);
    }

    // Delete student
    public void deleteStudent(Long id) {

        Student student = getStudentById(id);

        studentRepository.delete(student);
    }
}
