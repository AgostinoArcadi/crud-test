package com.example.crudtest;

import com.example.crudtest.entity.Student;
import com.example.crudtest.repository.StudentRepository;
import com.example.crudtest.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
public class StudentServiceTest {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Test
    void updateStudentWorking() {
        Student student = new Student(1, "Agostino", "Arcadi", true);

        Student personaFromDb = studentRepository.save(student);
        assertThat(personaFromDb).isNotNull();
        assertThat(personaFromDb.getId()).isNotNull();

        Student personaFromService = studentService.updateStudentWorking(student.getId(), true).get();
        assertThat(personaFromService).isNotNull();
        assertThat(personaFromService.getId()).isNotNull();
        assertThat(personaFromService.isWorking()).isTrue();

        Student personaFromFind = studentRepository.findById(student.getId()).get();
        assertThat(personaFromFind).isNotNull();
        assertThat(personaFromFind.getId()).isNotNull();
        assertThat(personaFromFind.getId()).isEqualTo(personaFromDb.getId());
        assertThat(personaFromFind.isWorking()).isTrue();
    }
}
