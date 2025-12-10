package com.example.crudtest;

import com.example.crudtest.controller.StudentController;
import com.example.crudtest.entity.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
class StudentControllerTest {

	@Autowired
	private StudentController studentController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

    private Student savedStudent;

    @BeforeEach
    void setUp() throws Exception{
        Student student = new Student(null, "Mario", "Rossi", true);
        String studentJson = objectMapper.writeValueAsString(student);

        MvcResult postResult = this.mockMvc.perform(post("/v1/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk())
                .andReturn();
        savedStudent = objectMapper.readValue(postResult.getResponse().getContentAsString(), Student.class);
    }

	@Test
	void contextLoads() {
		assertThat(studentController).isNotNull();
	}

	@Test
	void addStudent() throws Exception {
		Student student = new Student(null, "Luca", "Verdi", true);

        //objectMapper = classe di Jackson, converte oggetti Java in JSON e viceversa
		String studentJson = objectMapper.writeValueAsString(student);

        //mock.perform() = simula una chiamata HTTP al controller
        //and...() = controlli e debug
        MvcResult result = this.mockMvc.perform(post("/v1/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

        Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(studentFromResponse).isNotNull();
        assertThat(studentFromResponse.getId()).isNotNull();
        assertThat(studentFromResponse.getName()).isEqualTo(student.getName());
        assertThat(studentFromResponse.getSurname()).isEqualTo(student.getSurname());
        assertThat(studentFromResponse.isWorking()).isTrue();
	}

	@Test
	void findById() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/v1/student/" + savedStudent.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isEqualTo(savedStudent.getId());
        assertThat(studentFromResponse.getName()).isEqualTo("Mario");
        assertThat(studentFromResponse.getSurname()).isEqualTo("Rossi");
        assertThat(studentFromResponse.isWorking()).isTrue();
	}

	@Test
	void findAllStudent() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/v1/students"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentFromResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertThat(studentFromResponseList.size()).isNotZero();
	}

	@Test
	void updateStudent() throws Exception {
        Student student = new Student(null, "Agostino", "Arcadi", false);
		String studentJson = objectMapper.writeValueAsString(student);

		MvcResult result = this.mockMvc.perform(put("/v1/student/" + savedStudent.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(studentFromResponse).isNotNull();
        assertThat(studentFromResponse.getId()).isEqualTo(savedStudent.getId());
		assertThat(studentFromResponse.getName()).isEqualTo(student.getName());
		assertThat(studentFromResponse.getSurname()).isEqualTo(student.getSurname());
        assertThat(studentFromResponse.isWorking()).isTrue();
	}

	@Test
	void updateStudentWorking() throws Exception {
		MvcResult result = this.mockMvc.perform(put("/v1/work/" + savedStudent.getId() + "?working=" + "false"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse).isNotNull();
        assertThat(studentFromResponse.getId()).isEqualTo(savedStudent.getId());
        assertThat(studentFromResponse.getName()).isEqualTo("Mario");
        assertThat(studentFromResponse.getSurname()).isEqualTo("Rossi");
		assertThat(studentFromResponse.isWorking()).isFalse();
	}

	@Test
	void deleteStudent() throws Exception {
		mockMvc.perform(delete("/v1/student/" + savedStudent.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}
}
