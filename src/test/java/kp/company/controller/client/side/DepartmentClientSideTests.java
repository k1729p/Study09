package kp.company.controller.client.side;

import kp.company.TestDatasetLoader;
import kp.company.controller.client.side.base.ClientSideTestsBase;
import kp.company.domain.Department;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.lang.invoke.MethodHandles;

import static kp.Constants.COMPANY_PATH;
import static kp.Constants.DEPARTMENTS_PATH;
import static kp.TestConstants.*;

/**
 * Client-side tests for {@link Department}.
 * <p>
 * Full-stack integration test.
 * Loads a {@link org.springframework.boot.web.server.context.WebServerApplicationContext} and provides a real web environment.
 * </p>
 */
class DepartmentClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Finds the {@link Department} by its id.
     */
    @Test
    @DisplayName("🟩 should find department by id")
    void shouldFindDepartmentById() {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = String.format("%s%s/%d", COMPANY_PATH, DEPARTMENTS_PATH, EXPECTED_DEPARTMENT_ID);
        // WHEN
        final RestTestClient.ResponseSpec responseSpec = restClient.get().uri(urlTemplate).exchange();
        // THEN
        responseSpec.expectStatus().isOk();
        responseSpec.expectHeader().contentType("application/hal+json");
        final RestTestClient.BodyContentSpec bodyContentSpec = responseSpec.expectBody();
        bodyContentSpec.jsonPath("id").isEqualTo(String.valueOf(EXPECTED_DEPARTMENT_ID));
        bodyContentSpec.jsonPath("name").isEqualTo(EXPECTED_DEPARTMENT_NAME);
        bodyContentSpec.jsonPath("employees[0].id").isEqualTo(EXPECTED_EMPLOYEE_ID);
        bodyContentSpec.jsonPath("employees[0].firstName").isEqualTo(EXPECTED_EMPLOYEE_F_NAME);
        bodyContentSpec.jsonPath("employees[0].lastName").isEqualTo(EXPECTED_EMPLOYEE_L_NAME);
        final String actualHost = responseSpec.returnResult().getUrl().getHost();
        final int actualPort = responseSpec.returnResult().getUrl().getPort();
        final String expectedSelfLink = String.format("http://%s:%d%s%s/%d",
                actualHost, actualPort, COMPANY_PATH, DEPARTMENTS_PATH, EXPECTED_DEPARTMENT_ID);
        bodyContentSpec.jsonPath("_links.self.href").isEqualTo(expectedSelfLink);
        final String expectedParentLink = String.format("http://%s:%d%s%s",
                actualHost, actualPort, COMPANY_PATH, DEPARTMENTS_PATH);
        bodyContentSpec.jsonPath("_links.departments.href").isEqualTo(expectedParentLink);
        if (VERBOSE) {
            System.out.println(responseSpec.returnResult(String.class).getResponseBody());
        }
        logger.info("shouldFindDepartmentById():");
    }
}