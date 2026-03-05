package kp.company.controller.binding;

import kp.company.TestDatasetLoader;
import kp.company.domain.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.lang.invoke.MethodHandles;

import static kp.Constants.COMPANY_PATH;
import static kp.Constants.DEPARTMENTS_PATH;
import static kp.TestConstants.EXPECTED_DEPARTMENT_ID;

/**
 * The end-to-end testing with {@link RestTestClient#bindToServer}.
 *
 * <p>
 * Spring Boot server is STARTED.
 * Real embedded server on random port.
 * </p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestD {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @LocalServerPort
    private int port;
    private RestTestClient restClient;

    /**
     * Sets up test data and stubs before each test.
     */
    @BeforeEach
    void setUp() {
        TestDatasetLoader.loadTestDataset();
        restClient = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    /**
     * Finds the {@link Department} by its id.
     */
    @Test
    @DisplayName("🟩 should find department by id")
    void shouldFindDepartmentById() {
        // GIVEN
        final String requestUri = String.format("%s%s/%d", COMPANY_PATH, DEPARTMENTS_PATH, EXPECTED_DEPARTMENT_ID);
        // WHEN
        final RestTestClient.ResponseSpec responseSpec = restClient.get().uri(requestUri).exchange();
        // THEN
        TestHelper.checkResults(responseSpec);
        logger.info("shouldFindDepartmentById():");
    }
}
