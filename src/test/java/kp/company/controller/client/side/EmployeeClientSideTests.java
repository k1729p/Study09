package kp.company.controller.client.side;

import kp.company.TestDatasetLoader;
import kp.company.controller.client.side.base.ClientSideTestsBase;
import kp.company.domain.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.lang.invoke.MethodHandles;

import static kp.Constants.COMPANY_PATH;
import static kp.Constants.EMPLOYEES_PATH;
import static kp.TestConstants.*;

/**
 * Client-side tests for {@link Employee}.
 * <p>
 * Full-stack integration test.
 * Loads a {@link org.springframework.boot.web.server.context.WebServerApplicationContext} and provides a real web environment.
 * </p>
 */
class EmployeeClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Finds the {@link Employee} by its id.
     */
    @Test
    @DisplayName("🟩 should find employee by id")
    void shouldFindEmployeeById() {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s/%d".formatted(COMPANY_PATH, EMPLOYEES_PATH, EXPECTED_EMPLOYEE_ID);
        // WHEN
        final RestTestClient.ResponseSpec responseSpec =
                restClient.get().uri(urlTemplate).accept(MediaType.APPLICATION_JSON).exchange();
        // THEN
        responseSpec.expectStatus().isOk();
        responseSpec.expectHeader().contentType(MediaType.APPLICATION_JSON);
        final RestTestClient.BodyContentSpec bodyContentSpec = responseSpec.expectBody();
        bodyContentSpec.jsonPath("id").isEqualTo(String.valueOf(EXPECTED_EMPLOYEE_ID));
        bodyContentSpec.jsonPath("firstName").isEqualTo(EXPECTED_EMPLOYEE_F_NAME);
        bodyContentSpec.jsonPath("lastName").isEqualTo(EXPECTED_EMPLOYEE_L_NAME);
        bodyContentSpec.jsonPath("department.id").isEqualTo(String.valueOf(EXPECTED_DEPARTMENT_ID));
        bodyContentSpec.jsonPath("department.name").isEqualTo(EXPECTED_DEPARTMENT_NAME);
        final String actualHost = responseSpec.returnResult().getUrl().getHost();
        final int actualPort = responseSpec.returnResult().getUrl().getPort();
        final String expectedSelfLink = String.format("http://%s:%d%s%s/%d",
                actualHost, actualPort, COMPANY_PATH, EMPLOYEES_PATH, EXPECTED_EMPLOYEE_ID);
        bodyContentSpec.jsonPath("_links.self.href").isEqualTo(expectedSelfLink);
        final String expectedParentLink = String.format("http://%s:%d%s%s",
                actualHost, actualPort, COMPANY_PATH, EMPLOYEES_PATH);
        bodyContentSpec.jsonPath("_links.employees.href").isEqualTo(expectedParentLink);
        if (VERBOSE) {
            System.out.println(responseSpec.returnResult(String.class).getResponseBody());
        }
        logger.info("shouldFindEmployeeById():");
    }
}