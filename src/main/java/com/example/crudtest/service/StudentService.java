package com.example.crudtest.service;

import com.example.crudtest.entity.Student;
import com.example.crudtest.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public Student addStudent(Student student) {
        studentRepository.save(student);
        return student;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Integer id) {
        Optional<Student> studentOpt = studentRepository.findById(id);

        if(studentOpt.isPresent()) {
            return studentOpt;
        }
        return Optional.empty();
    }

    public Optional<Student> updateStudent(Student student, Integer id) {
        Optional<Student> studentOpt = studentRepository.findById(id);

        if(studentOpt.isPresent()) {
            studentOpt.get().setName(student.getName());
            studentOpt.get().setSurname(student.getSurname());

            Student studentUpdated = studentRepository.save(studentOpt.get());
            return Optional.of(studentUpdated);
        }
        return Optional.empty();
    }

    public Optional<Student> updateStudentWorking(Integer id, boolean isWorking) {
        Optional<Student> student = studentRepository.findById(id);

        if(student.isPresent()) {
            student.get().setWorking(isWorking);

            Student studentUpdated = studentRepository.save(student.get());
            return Optional.of(studentUpdated);
        }
        return Optional.empty();
    }

    public Optional<Student> deleteStudent(Integer id) {
        Optional<Student> studentOpt = studentRepository.findById(id);

        if(studentOpt.isPresent()) {
            studentRepository.deleteById(id);
            return studentOpt;
        }
        return Optional.empty();
    }

}
