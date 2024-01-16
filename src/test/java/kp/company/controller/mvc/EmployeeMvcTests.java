package kp.company.controller.mvc;

import static kp.TestConstants.ABSENT_EMPLOYEE_ID;
import static kp.TestConstants.CREATED_EMPLOYEE_CONTENT;
import static kp.TestConstants.CREATED_EMPLOYEE_F_NAME;
import static kp.TestConstants.CREATED_EMPLOYEE_ID;
import static kp.TestConstants.CREATED_EMPLOYEE_L_NAME;
import static kp.TestConstants.DEP_TEST_SIZE;
import static kp.TestConstants.EMP_TEST_SIZE;
import static kp.TestConstants.EXPECTED_DEPARTMENT_ID;
import static kp.TestConstants.EXPECTED_DEPARTMENT_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_F_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_ID;
import static kp.TestConstants.EXPECTED_EMPLOYEE_L_NAME;
import static kp.TestConstants.FIND_EMP_BY_ID_HREF;
import static kp.TestConstants.FIND_EMP_HREF;
import static kp.TestConstants.ROOT_URL;
import static kp.TestConstants.UPDATED_EMPLOYEE_CONTENT;
import static kp.TestConstants.UPDATED_EMPLOYEE_F_NAME;
import static kp.TestConstants.UPDATED_EMPLOYEE_L_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import kp.Constants;
import kp.company.TestDatasetLoader;
import kp.company.domain.Employee;

/**
 * The tests for the {@link Employee} with server-side support using
 * {@link MockMvc}.<br>
 * The server is <b>not started</b>.
 * <p>
 * These tests use the 'Spring MVC Test Framework'.<br>
 * Loads a web application context and provides a mock web environment.<br>
 * The full Spring application context is started, but without the server.
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeMvcTests {

	@Autowired
	private MockMvc mockMvc;

	private static final boolean VERBOSE = false;

	/**
	 * The constructor.
	 */
	EmployeeMvcTests() {
		super();
	}

	/**
	 * Deletes test dataset.
	 * 
	 */
	@AfterEach
	void deleteTestDataset() {
		TestDatasetLoader.deleteTestDataset();
	}

	/**
	 * Should find all the {@link Employee}s.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldFindAllEmployees() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s", ROOT_URL, Constants.COMPANY_PATH, Constants.EMPLOYEES_PATH);
		final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isOk());
		resultActions.andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
		resultActions.andExpect(jsonPath("_embedded.employeeList").isArray());
		resultActions.andExpect(jsonPath("_embedded.employeeList", Matchers.hasSize(DEP_TEST_SIZE * EMP_TEST_SIZE)));
		resultActions.andExpect(jsonPath("_embedded.employeeList[0].id").value(EXPECTED_EMPLOYEE_ID));
		resultActions.andExpect(jsonPath("_embedded.employeeList[0].firstName").value(EXPECTED_EMPLOYEE_F_NAME));
		resultActions.andExpect(jsonPath("_embedded.employeeList[0].lastName").value(EXPECTED_EMPLOYEE_L_NAME));
		resultActions.andExpect(jsonPath("_embedded.employeeList[0].department").exists());
		resultActions.andExpect(jsonPath("_embedded.employeeList[0].department.id").value(EXPECTED_DEPARTMENT_ID));
		resultActions.andExpect(jsonPath("_embedded.employeeList[0].department.name").value(EXPECTED_DEPARTMENT_NAME));
		resultActions.andExpect(jsonPath("_embedded.employeeList[0]._links.self.href").value(FIND_EMP_BY_ID_HREF));
		resultActions.andExpect(jsonPath("_embedded.employeeList[0]._links.employees.href").value(FIND_EMP_HREF));
	}

	/**
	 * Should get '404 Not Found' error response when searching with the absent
	 * {@link Employee}.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetNotFoundErrorWhenSearchingWithAbsentEmployee() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/%d", ROOT_URL, Constants.COMPANY_PATH,
				Constants.EMPLOYEES_PATH, ABSENT_EMPLOYEE_ID);
		final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(UPDATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isNotFound());
	}

	/**
	 * Should get '400 Bad Request' error response when searching with bad id.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetBadRequestErrorWhenSearchingWithBadId() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/ABC", ROOT_URL, Constants.COMPANY_PATH,
				Constants.EMPLOYEES_PATH);
		final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(UPDATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isBadRequest());
	}

	/**
	 * Should create the {@link Employee}.<br>
	 * Tests a <b>POST</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldCreateEmployee() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDatasetOnlyWithDepartment();
		final String urlTemplate = String.format("%s%s%s", ROOT_URL, Constants.COMPANY_PATH, Constants.EMPLOYEES_PATH);
		final MockHttpServletRequestBuilder requestBuilder = post(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(CREATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isCreated());
		final String createdEmployeeUrlTemplate = String.format("%s/%d", urlTemplate, CREATED_EMPLOYEE_ID);
		resultActions.andExpect(header().string(HttpHeaders.LOCATION, createdEmployeeUrlTemplate));
		resultActions.andExpect(redirectedUrl(createdEmployeeUrlTemplate));
		getAndCheckEmployee(createdEmployeeUrlTemplate, CREATED_EMPLOYEE_F_NAME, CREATED_EMPLOYEE_L_NAME);
	}

	/**
	 * Should update the {@link Employee}.<br>
	 * Tests a <b>PATCH</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldUpdateEmployee() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/%d", ROOT_URL, Constants.COMPANY_PATH,
				Constants.EMPLOYEES_PATH, EXPECTED_EMPLOYEE_ID);
		final MockHttpServletRequestBuilder requestBuilder = patch(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(UPDATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isCreated());
		resultActions.andExpect(header().string(HttpHeaders.LOCATION, urlTemplate));
		resultActions.andExpect(redirectedUrl(urlTemplate));
		getAndCheckEmployee(urlTemplate, UPDATED_EMPLOYEE_F_NAME, UPDATED_EMPLOYEE_L_NAME);
	}

	/**
	 * Should get '404 Not Found' error response when updating with the absent
	 * {@link Employee}.<br>
	 * Should not update the absent {@link Employee}.<br>
	 * Tests a <b>PATCH</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetNotFoundErrorWhenUpdatingWithAbsentEmployee() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/%d", ROOT_URL, Constants.COMPANY_PATH,
				Constants.EMPLOYEES_PATH, ABSENT_EMPLOYEE_ID);
		final MockHttpServletRequestBuilder requestBuilder = patch(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(UPDATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isNotFound());
	}

	/**
	 * Should delete the {@link Employee}.<br>
	 * Tests a <b>DELETE</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldDeleteEmployee() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/%d", ROOT_URL, Constants.COMPANY_PATH,
				Constants.EMPLOYEES_PATH, EXPECTED_EMPLOYEE_ID);
		final MockHttpServletRequestBuilder requestBuilder = delete(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isNoContent());
		getAndCheckDeletedEmployee(urlTemplate);
	}

	/**
	 * Should retrieve and check the {@link Employee}.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @param urlTemplate               the URL template
	 * @param expectedEmployeeFirstName the expected {@link Employee} first name
	 * @param expectedEmployeeLastName  the expected {@link Employee} last name
	 * @throws Exception the {@link Exception}
	 */
	private void getAndCheckEmployee(String urlTemplate, String expectedEmployeeFirstName,
			String expectedEmployeeLastName) throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isOk());
		resultActions.andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
		resultActions.andExpect(jsonPath("firstName").value(expectedEmployeeFirstName));
		resultActions.andExpect(jsonPath("lastName").value(expectedEmployeeLastName));
		resultActions.andExpect(jsonPath("_links.self.href").value(urlTemplate));
		resultActions.andExpect(jsonPath("_links.employees.href").value(FIND_EMP_HREF));
	}

	/**
	 * Should not find the deleted {@link Employee}.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @param urlTemplate the URL template
	 * @throws Exception the {@link Exception}
	 */
	private void getAndCheckDeletedEmployee(String urlTemplate) throws Exception {
		// GIVEN
		final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isNotFound());
	}

}