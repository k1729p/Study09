package kp.company.controller;

import static kp.Constants.DEP_INDEX_UPPER_BOUND;
import static kp.Constants.EMP_INDEX_UPPER_BOUND;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kp.Constants;
import kp.SampleDataset;

/**
 * The sample dataset loader controller.
 */
@RestController
@RequestMapping(Constants.COMPANY_PATH)
public class SampleDatasetLoaderController {
	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass().getName());

	/**
	 * The constructor.
	 * 
	 */
	public SampleDatasetLoaderController() {
		super();
	}

	/**
	 * Loads the sample dataset for the departments with the employees.
	 * 
	 * @param depIndex the department's index upper bound
	 * @param empIndex the employee's index upper bound
	 * @return the dataset loading confirmation response
	 */
	@GetMapping(Constants.LOAD_SAMPLE_DATASET_PATH)
	public String loadSampleDataset(Optional<Long> depIndex, Optional<Long> empIndex) {

		SampleDataset.loadDataset(depIndex.orElse(DEP_INDEX_UPPER_BOUND), empIndex.orElse(EMP_INDEX_UPPER_BOUND));
		logger.debug("loadSampleDataset():");
		return Constants.LOAD_SAMPLE_DATASET_RESULT;
	}

}
