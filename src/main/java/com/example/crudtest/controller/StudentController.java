package com.example.crudtest.controller;

import com.example.crudtest.entity.Student;
import com.example.crudtest.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/student")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        studentService.addStudent(student);
        return ResponseEntity.ok().body(student);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Student> findById(@PathVariable Integer id) {
        Optional<Student> student = studentService.findById(id);

        if(student.isPresent()){
            return ResponseEntity.ok().body(student.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> findAllStudents() {
        List<Student> students = studentService.findAll();
        return ResponseEntity.ok().body(students);
    }

    @PutMapping("/student/{id}")
    private ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        Optional<Student> studentFound = studentService.updateStudent(student, id);

        if(studentFound.isPresent()){
            return ResponseEntity.ok().body(studentFound.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/work/{id}")
    public ResponseEntity<Student> updateStudentWorking(@PathVariable Integer id, @RequestParam boolean working) {
        Optional<Student> studentFound = studentService.updateStudentWorking(id, working);

        if(studentFound.isPresent()){
            return ResponseEntity.ok().body(studentFound.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Integer id) {
        Optional<Student> studentFound = studentService.deleteStudent(id);

        if(studentFound.isPresent()){
            return ResponseEntity.ok().body(studentFound.get());
        }
        return ResponseEntity.notFound().build();
    }

}
