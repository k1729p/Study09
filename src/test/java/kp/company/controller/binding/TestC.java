package kp.company.controller.binding;

import kp.company.TestDatasetLoader;
import kp.company.domain.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.lang.invoke.MethodHandles;

import static kp.Constants.COMPANY_PATH;
import static kp.Constants.DEPARTMENTS_PATH;
import static kp.TestConstants.EXPECTED_DEPARTMENT_ID;

/**
 * The integration testing with {@link RestTestClient#bindToApplicationContext}.
 *
 * <p>
 * Spring Boot server is STARTED.
 * Real embedded server on random port.
 * </p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestC {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private WebApplicationContext context;
    private RestTestClient restClient;

    /**
     * Sets up test data and stubs before each test.
     */
    @BeforeEach
    void setUp() {
        TestDatasetLoader.loadTestDataset();
        restClient = RestTestClient.bindToApplicationContext(context).build();
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
