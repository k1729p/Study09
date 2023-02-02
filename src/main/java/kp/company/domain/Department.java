package kp.company.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import kp.Constants;

/**
 * Simple JavaBean domain object representing a department.
 *
 * @param id        the id
 * @param name      the name
 * @param employees the list of the {@link Employee}s
 */
public record Department(long id, String name, @JsonIgnoreProperties("department") List<Employee> employees) {

	/**
	 * Creates the department from the index.
	 * 
	 * @param depIndex the department index
	 * @return the department
	 */
	public static Department fromIndex(long depIndex) {
		return new Department(depIndex, Constants.DEP_NAME_FUN.apply(depIndex), new ArrayList<>());
	}
}
