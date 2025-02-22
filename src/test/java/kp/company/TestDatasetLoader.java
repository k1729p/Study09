package kp.company;

import kp.SampleDataset;
import kp.company.domain.Department;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static kp.TestConstants.*;

/**
 * The loader for the test dataset.
 */
@Component
public class TestDatasetLoader {

    /**
     * Loads the test dataset.
     */
    public static void loadTestDataset() {
        SampleDataset.loadDataset(DEP_TEST_INDEX_LOWER_BOUND, DEP_TEST_INDEX_UPPER_BOUND, EMP_TEST_INDEX_LOWER_BOUND,
                EMP_TEST_INDEX_UPPER_BOUND);
    }

    /**
     * Loads the test dataset only with the department.
     */
    public static void loadTestDatasetOnlyWithDepartment() {
        SampleDataset.saveDepartment(new Department(CREATED_DEPARTMENT_ID, CREATED_DEPARTMENT_NAME, new ArrayList<>()));
    }

    /**
     * Deletes the test dataset.
     */
    public static void deleteTestDataset() {
        SampleDataset.deleteDataset();
    }

}
