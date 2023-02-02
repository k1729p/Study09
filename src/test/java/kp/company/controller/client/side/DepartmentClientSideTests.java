package kp.company.controller.client.side;

import static kp.TestConstants.EXPECTED_DEPARTMENT_ID;
import static kp.TestConstants.EXPECTED_DEPARTMENT_NAME;
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
import kp.company.domain.Department;

/**
 * The client side tests for the {@link Department} using
 * {@link TestRestTemplate}.<br>
 * The server is <b>started</b>. The full-stack integration test.<br>
 * Loads a
 * {@link org.springframework.boot.web.context.WebServerApplicationContext} and
 * provides a real web environment.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DepartmentClientSideTests {
	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass().getName());

	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * The constructor.
	 */
	DepartmentClientSideTests() {
		super();
	}

	/**
	 * Should find the {@link Department} by the id.
	 */
	@Test
	void shouldFindDepartmentById() {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s/%d", Constants.COMPANY_PATH, Constants.DEPARTMENTS_PATH,
				EXPECTED_DEPARTMENT_ID);
		// WHEN
		final ResponseEntity<Department> responseEntity = restTemplate.getForEntity(urlTemplate, Department.class);
		// THEN
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		final Department department = responseEntity.getBody();
		Assertions.assertThat(department).isNotNull();
		assertEquals(EXPECTED_DEPARTMENT_ID, department.id());
		assertEquals(EXPECTED_DEPARTMENT_NAME, department.name());
		logger.debug("shouldFindDepartmentById():");
	}
}