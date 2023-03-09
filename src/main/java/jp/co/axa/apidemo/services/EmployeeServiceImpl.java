package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/*
 * Service for doing CRUD for employee api
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);

	@Cacheable("employess")
	public List<Employee> retrieveEmployees() {
		List<Employee> employess = employeeRepository.findAll();
		return employess;
	}

	@Cacheable("employee")
	public Employee getEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee dosen't exist with given id:" + employeeId));
		logger.info("Employee are successfully fetched from the DB");
		return employee;

	}

	@Caching(evict = { @CacheEvict(value = "employee", allEntries = true),
			@CacheEvict(value = "employees", allEntries = true), })
	public Employee saveEmployee(Employee employee) {
		Optional<Employee> savedEmployee = employeeRepository.findById(employee.getId());
		if (savedEmployee.isPresent()) {
			logger.error("An exception occurred!",
					new ResourceNotFoundException("Employee already exist with given id:" + employee.getId()));
			throw new ResourceNotFoundException("Employee already exist with given id:" + employee.getId());

		}
		return employeeRepository.save(employee);
	}

	@Caching(evict = { @CacheEvict(value = "employee", allEntries = true),
			@CacheEvict(value = "employees", allEntries = true), })
	public void deleteEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee dosen't exist with given id:" + employeeId));
		employeeRepository.deleteById(employee.getId());
		logger.info("Deletion of employee is successfully done");
	}

	@Caching(evict = { @CacheEvict(value = "employee", allEntries = true),
			@CacheEvict(value = "employees", allEntries = true), })
	public void updateEmployee(Employee employee) {
		employeeRepository.save(employee);
		logger.info("Updation of employee is successfully done");
	}
}