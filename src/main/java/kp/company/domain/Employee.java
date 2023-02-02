package kp.company.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import kp.Constants;

/**
 * Simple JavaBean domain object representing an employee.
 * 
 * @param id         the id
 * @param firstName  the first name
 * @param lastName   the last name
 * @param department the {@link Department}
 */
public record Employee(long id, String firstName, String lastName,
		@JsonIgnoreProperties("employees") Department department) {

	/**
	 * Creates the employee from the {@link Department} index and the employee
	 * index.
	 * 
	 * @param department the {@link Department}
	 * @param empIndex   the employee index
	 * @return the employee
	 */
	public static Employee fromIndexes(Department department, long empIndex) {
		return new Employee(Constants.EMP_INDEX_FUN.applyAsLong(department.id(), empIndex),
				Constants.EMP_F_NAME_FUN.apply(department.id(), empIndex),
				Constants.EMP_L_NAME_FUN.apply(department.id(), empIndex), department);
	}

}
