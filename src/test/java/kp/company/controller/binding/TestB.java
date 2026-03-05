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
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.lang.invoke.MethodHandles;

import static kp.Constants.COMPANY_PATH;
import static kp.Constants.DEPARTMENTS_PATH;
import static kp.TestConstants.EXPECTED_DEPARTMENT_ID;

/**
 * Spring MVC testing with {@link RestTestClient#bindTo} and {@link MockMvc}.
 *
 * <p>
 * Spring Boot server is STARTED.
 * The MockMvc is plugged in as the server to handle requests
 * (instead of a real server).
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TestB {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private MockMvc mockMvc;
    private RestTestClient restClient;

    /**
     * Sets up test data and stubs before each test.
     */
    @BeforeEach
    void setUp() {
        TestDatasetLoader.loadTestDataset();
        restClient = RestTestClient.bindTo(mockMvc).build();
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
