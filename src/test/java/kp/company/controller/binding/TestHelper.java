package kp.company.controller.binding;

import org.springframework.test.web.servlet.client.RestTestClient;

import static kp.TestConstants.EXPECTED_DEPARTMENT_ID;
import static kp.TestConstants.EXPECTED_DEPARTMENT_NAME;

/**
 * The test helper.
 */
public class TestHelper {
    /**
     * Checks the test results.
     *
     * @param responseSpec the response specification
     */
    static void checkResults(RestTestClient.ResponseSpec responseSpec) {

        responseSpec.expectStatus().isOk();
        final RestTestClient.BodyContentSpec bodyContentSpec = responseSpec.expectBody();
        bodyContentSpec.jsonPath("id").isEqualTo(String.valueOf(EXPECTED_DEPARTMENT_ID));
        bodyContentSpec.jsonPath("name").isEqualTo(EXPECTED_DEPARTMENT_NAME);

    }
}
