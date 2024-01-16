package kp.company.controller.mvc;

import static kp.TestConstants.ABSENT_DEPARTMENT_ID;
import static kp.TestConstants.CREATED_DEPARTMENT_CONTENT;
import static kp.TestConstants.CREATED_DEPARTMENT_ID;
import static kp.TestConstants.CREATED_DEPARTMENT_NAME;
import static kp.TestConstants.DEP_TEST_SIZE;
import static kp.TestConstants.EXPECTED_DEPARTMENT_ID;
import static kp.TestConstants.EXPECTED_DEPARTMENT_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_F_NAME;
import static kp.TestConstants.EXPECTED_EMPLOYEE_ID;
import static kp.TestConstants.EXPECTED_EMPLOYEE_L_NAME;
import static kp.TestConstants.FIND_DEP_HREF;
import static kp.TestConstants.ROOT_URL;
import static kp.TestConstants.UPDATED_DEPARTMENT_CONTENT;
import static kp.TestConstants.UPDATED_DEPARTMENT_NAME;
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
import kp.company.domain.Department;

/**
 * The tests for the {@link Department} with server-side support using
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
class DepartmentMvcTests {

	@Autowired
	private MockMvc mockMvc;

	private static final boolean VERBOSE = false;

	/**
	 * The constructor.
	 */
	DepartmentMvcTests() {
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
	 * Should find all the {@link Department}s on the third page.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldFindAllDepartments() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s", ROOT_URL, Constants.COMPANY_PATH,
				Constants.DEPARTMENTS_PATH);
		final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isOk());
		resultActions.andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
		resultActions.andExpect(jsonPath("_embedded.departmentList").isArray());
		resultActions.andExpect(jsonPath("_embedded.departmentList", Matchers.hasSize(DEP_TEST_SIZE)));
		resultActions
				.andExpect(jsonPath("_embedded.departmentList[0].id").value(Long.toString(EXPECTED_DEPARTMENT_ID)));
		resultActions.andExpect(jsonPath("_embedded.departmentList[0].name").value(EXPECTED_DEPARTMENT_NAME));
		resultActions.andExpect(jsonPath("_embedded.departmentList[0].employees[0].id").value(EXPECTED_EMPLOYEE_ID));
		resultActions.andExpect(
				jsonPath("_embedded.departmentList[0].employees[0].firstName").value(EXPECTED_EMPLOYEE_F_NAME));
		resultActions.andExpect(
				jsonPath("_embedded.departmentList[0].employees[0].lastName").value(EXPECTED_EMPLOYEE_L_NAME));
		resultActions.andExpect(jsonPath("_embedded.departmentList[0]._links.self.href").value(urlTemplate + "/1"));
		resultActions.andExpect(jsonPath("_embedded.departmentList[0]._links.departments.href").value(urlTemplate));
	}

	/**
	 * Should get '404 Not Found' error response when searching with the absent
	 * {@link Department}.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetNotFoundErrorWhenSearchingWithAbsentDepartment() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/%d", ROOT_URL, Constants.COMPANY_PATH,
				Constants.DEPARTMENTS_PATH, ABSENT_DEPARTMENT_ID);
		final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(UPDATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
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
				Constants.DEPARTMENTS_PATH);
		final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(UPDATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isBadRequest());
	}

	/**
	 * Should create the {@link Department}.<br>
	 * Tests a <b>POST</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldCreateDepartment() throws Exception {
		// GIVEN
		final String urlTemplate = String.format("%s%s%s", ROOT_URL, Constants.COMPANY_PATH,
				Constants.DEPARTMENTS_PATH);
		final MockHttpServletRequestBuilder requestBuilder = post(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(CREATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isCreated());
		final String createdDepartmentUrlTemplate = String.format("%s/%d", urlTemplate, CREATED_DEPARTMENT_ID);
		resultActions.andExpect(header().string(HttpHeaders.LOCATION, createdDepartmentUrlTemplate));
		resultActions.andExpect(redirectedUrl(createdDepartmentUrlTemplate));
		getAndCheckDepartment(createdDepartmentUrlTemplate, CREATED_DEPARTMENT_NAME);
	}

	/**
	 * Should update the {@link Department}.<br>
	 * Tests a <b>PATCH</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldUpdateDepartment() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/%d", ROOT_URL, Constants.COMPANY_PATH,
				Constants.DEPARTMENTS_PATH, EXPECTED_DEPARTMENT_ID);
		final MockHttpServletRequestBuilder requestBuilder = patch(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(UPDATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isCreated());
		resultActions.andExpect(header().string(HttpHeaders.LOCATION, urlTemplate));
		resultActions.andExpect(redirectedUrl(urlTemplate));
		getAndCheckDepartment(urlTemplate, UPDATED_DEPARTMENT_NAME);
	}

	/**
	 * Should get '404 Not Found' error response when updating the absent
	 * {@link Department}.<br>
	 * Tests a <b>PATCH</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldGetNotFoundErrorWhenUpdatingAbsentDepartment() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/%d", ROOT_URL, Constants.COMPANY_PATH,
				Constants.DEPARTMENTS_PATH, ABSENT_DEPARTMENT_ID);
		final MockHttpServletRequestBuilder requestBuilder = patch(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
				.content(UPDATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isNotFound());
	}

	/**
	 * Should delete the {@link Department}.<br>
	 * Tests a <b>DELETE</b> request.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@Test
	void shouldDeleteDepartment() throws Exception {
		// GIVEN
		TestDatasetLoader.loadTestDataset();
		final String urlTemplate = String.format("%s%s%s/%d", ROOT_URL, Constants.COMPANY_PATH,
				Constants.DEPARTMENTS_PATH, EXPECTED_DEPARTMENT_ID);
		final MockHttpServletRequestBuilder requestBuilder = delete(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
		// WHEN
		final ResultActions resultActions = mockMvc.perform(requestBuilder);
		// THEN
		if (VERBOSE) {
			resultActions.andDo(print());
		}
		resultActions.andExpect(status().isNoContent());
		getAndCheckDeletedDepartment(urlTemplate);
	}

	/**
	 * Should retrieve and check the {@link Department}.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @param urlTemplate            the URL template
	 * @param expectedDepartmentName the expected {@link Department} name
	 * @throws Exception the {@link Exception}
	 */
	private void getAndCheckDepartment(String urlTemplate, String expectedDepartmentName) throws Exception {
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
		resultActions.andExpect(jsonPath("name").value(expectedDepartmentName));
		resultActions.andExpect(jsonPath("_links.self.href").value(urlTemplate));
		resultActions.andExpect(jsonPath("_links.departments.href").value(FIND_DEP_HREF));
	}

	/**
	 * Should not find the deleted {@link Department}.<br>
	 * Tests a <b>GET</b> request.
	 * 
	 * @param urlTemplate the URL template
	 * @throws Exception the {@link Exception}
	 */
	private void getAndCheckDeletedDepartment(String urlTemplate) throws Exception {
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