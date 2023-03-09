package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Validated
public class EmployeeController {
	Logger logger = LogManager.getLogger(EmployeeController.class);
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	EmployeeRepository employeeRepository;

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/employees")
	public List<Employee> getEmployees() {
		logger.info("Get all employees endpoint get called");
		return employeeService.retrieveEmployees();
	}

	@GetMapping("/employees/{employeeId}")
	public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		logger.info("Get employee by id endpoint get called");
		Employee employee = employeeService.getEmployee(employeeId);
		return ResponseEntity.ok().body(employee);

	}

	@PostMapping("/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee saveEmployee(@Valid @RequestBody Employee employee) {
		logger.info("Save employee endpoint get called");
		return employeeService.saveEmployee(employee);
	}

	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		logger.info("Delete employee endpoint get called");
		employeeService.deleteEmployee(employeeId);
		System.out.println("Employee Deleted Successfully");
		return ResponseEntity.ok().body("Employee deleted with success!");
	}

	@PutMapping("/employees/{employeeId}")
	public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee,
			@PathVariable(name = "employeeId") Long employeeId) {
		logger.info("Update employee vy id endpoint get called");
		Employee emp = employeeService.getEmployee(employeeId);
		employee.setId(emp.getId());
		employeeService.updateEmployee(employee);
		return ResponseEntity.ok().body(employee);
	}

}
