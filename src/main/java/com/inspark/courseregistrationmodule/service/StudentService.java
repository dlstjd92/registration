package com.inspark.courseregistrationmodule.service;


import com.inspark.courseregistrationmodule.domain.Student;
import com.inspark.courseregistrationmodule.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public void addStudent(Student student){
        if (student == null)
            throw new IllegalArgumentException("Student cannot be null");

        boolean exists = studentRepository.existsByEmail(student.getEmail());
        if (exists)
            throw new IllegalArgumentException("Email already exists");

        studentRepository.save(student);
    }
}
