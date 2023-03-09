package jp.co.axa.apidemo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	// Deleting all the data at the start up
	@BeforeEach
	void setup() {
		employeeRepository.deleteAll();
	}

	// mocking employee object.
	// returning the created employee object
	@Test
	public void createEmployee_thenReturn200() throws Exception {
		// given - precondition or setup
		Employee employee = Employee.builder().id(1L).name("Ramesh").salary("10000").department("Tech").build();
		ResultActions response = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		// verify the result or output using assert statements
		response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(employee.getName())))
				.andExpect(jsonPath("$.salary", is(employee.getSalary())))
				.andExpect(jsonPath("$.department", is(employee.getDepartment())));

	}

	// JUnit Test Get All employees REST API
	@Test
	public void givenListOfEmployees_thenReturn200() throws Exception {
		// precondition or setup
		List<Employee> listOfEmployees = new ArrayList<>();
		listOfEmployees.add(Employee.builder().name("Ramesh").salary("10000").department("Tech").build());
		listOfEmployees
				.add(Employee.builder().name("Himanshu").salary("20000").department("Professional Services").build());
		employeeRepository.saveAll(listOfEmployees);
		employeeRepository.saveAll(listOfEmployees);
		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/v1/employees"));
		// verify the output
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

	}

	// positive scenario - valid employee id
	// JUnit test for GET employee by id REST API
	@Test
	public void getEmployeeById_thenReturn200() throws Exception {
		// precondition or setup
		Employee employee = Employee.builder().name("Ramesh").salary("10000").department("Tech").build();
		employeeRepository.save(employee);
		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/v1/employees/{employeeId}", employee.getId()));
		// verify the output
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.name", is(employee.getName())))
				.andExpect(jsonPath("$.salary", is(employee.getSalary())))
				.andExpect(jsonPath("$.department", is(employee.getDepartment())));

	}

	// negative scenario - invalid employee id
	// JUnit test for GET employee by id REST API
	@Test
	public void givenInvalidEmployeeId_thenReturn404() throws Exception {
		// precondition
		long employeeId = 2L;
		Employee employee = Employee.builder().name("Ramesh").salary("10000").department("Tech").build();
		employeeRepository.save(employee);
		// action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/v1/employees/{employeeId}", employeeId));
		// verify the output
		response.andExpect(status().isNotFound()).andDo(print());

	}

	// JUnit test for update employee REST API - positive scenario
	@Test
	public void givenUpdatedEmployee_thenReturn200() throws Exception {
		// precondition or setup
		Employee savedEmployee = Employee.builder().name("Ramesh").salary("10000").department("Tech").build();
		employeeRepository.save(savedEmployee);
		Employee updatedEmployee = Employee.builder().name("Himanshu").salary("20000").department("Tech").build();
		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{employeeId}", savedEmployee.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));
		// verify the output
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.name", is(updatedEmployee.getName())))
				.andExpect(jsonPath("$.salary", is(updatedEmployee.getSalary())))
				.andExpect(jsonPath("$.department", is(updatedEmployee.getDepartment())));
	}

	@Test
	public void givenInvalidUpdatedEmployee_thenReturn404() throws Exception {
		// precondition or setup
		long employeeId = 2L;
		Employee savedEmployee = Employee.builder().name("Ramesh").salary("10000").department("Tech").build();
		employeeRepository.save(savedEmployee);
		Employee updatedEmployee = Employee.builder().firstName("Himanshu").lastName("20000").email("Tech").build();
		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(put("/api/v1/employees/{employeeId}", employeeId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));
		// verify the output
		response.andExpect(status().isNotFound()).andDo(print());
	}

	// JUnit test for delete employee REST API
	@Test
	public void deleteByEmployeeId_thenReturn200() throws Exception {
		// given - precondition or setup
		// precondition or setup
		Employee savedEmployee = Employee.builder().name("Ramesh").salary("10000").department("Tech").build();
		employeeRepository.save(savedEmployee);
		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(delete("/api/v1/employees/{employeeId}", savedEmployee.getId()));
		// verify the output
		response.andExpect(status().isOk()).andDo(print());
	}

}