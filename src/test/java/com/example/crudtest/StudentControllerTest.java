package com.example.crudtest;

import com.example.crudtest.controller.StudentController;
import com.example.crudtest.entity.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class StudentControllerTest {

	@Autowired
	private StudentController studentController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
		assertThat(studentController).isNotNull();
	}

	@Test
	void addStudent() throws Exception {
		Student student = new Student(1, "Mario", "Rossi", true);

		String studentJson = objectMapper.writeValueAsString(student);

		this.mockMvc.perform(post("/v1/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void findById() throws Exception {
		int id = 1;
		addStudent();

		MvcResult result = this.mockMvc.perform(get("/v1/student/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse.getId()).isEqualTo(id);
	}

	@Test
	void findAllStudent() throws Exception {
		addStudent();

		MvcResult result = this.mockMvc.perform(get("/v1/students"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentFromResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertThat(studentFromResponseList.size()).isNotZero();
	}

	@Test
	void updateStudent() throws Exception {
		int id = 1;
		addStudent();

		Student student = new Student(3, "Agostino", "Arcadi", false);
		String studentJson = objectMapper.writeValueAsString(student);

		MvcResult result = this.mockMvc.perform(put("/v1/student/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse.getName()).isEqualTo(student.getName());
		assertThat(studentFromResponse.getSurname()).isEqualTo(student.getSurname());
	}

	@Test
	void updateStudentWorking() throws Exception {
		addStudent();
		int id = 1;
		boolean isWorking = false;

		MvcResult result = this.mockMvc.perform(put("/v1/work/" + id + "?working=" + isWorking))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.isWorking()).isEqualTo(false);
	}

	@Test
	void deleteStudent() throws Exception {
		addStudent();
		int id = 1;

		mockMvc.perform(delete("/v1/student/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}
}
