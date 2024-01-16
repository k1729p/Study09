package kp.company.controller.client.side;

import static kp.TestConstants.EXPECTED_EMPLOYEE_F_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_ID;
import static kp.TestConstants.EXPECTED_EMPLOYEE_L_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import kp.Constants;
import kp.company.TestDatasetLoader;
import kp.company.domain.Employee;

/**
 * The client side tests for the {@link Employee} using
 * {@link TestRestTemplate}.<br>
 * The server is <b>started</b>. The full-stack integration test.<br>
 * Loads a
 * {@link org.springframework.boot.web.context.WebServerApplicationContext} and
 * provides a real web environment.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeClientSideTests {
	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass().getName());

	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * The constructor.
	 */
	EmployeeClientSideTests() {
		super();
	}

	/**
	 * Should find the {@link Employee} by the id.
	 */
	@Test
	void shouldFindEmployeeById() {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s/%d", Constants.COMPANY_PATH, Constants.EMPLOYEES_PATH,
				EXPECTED_EMPLOYEE_ID);
		// WHEN
		final ResponseEntity<Employee> responseEntity = restTemplate.getForEntity(urlTemplate, Employee.class);
		// THEN
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		final Employee employee = responseEntity.getBody();
		Assertions.assertThat(employee).isNotNull();
		assertEquals(EXPECTED_EMPLOYEE_ID, employee.getId());
		assertEquals(EXPECTED_EMPLOYEE_F_NAME, employee.getFirstName());
		assertEquals(EXPECTED_EMPLOYEE_L_NAME, employee.getLastName());
		logger.debug("shouldFindEmployeeById():");
	}
}