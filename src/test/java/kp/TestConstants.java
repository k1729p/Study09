package kp;

/**
 * Test constants.
 *
 */
@SuppressWarnings("doclint:missing")
public final class TestConstants {
	public static final String ROOT_URL = "http://localhost";

	public static final long DEP_TEST_INDEX_LOWER_BOUND = 1;
	public static final long DEP_TEST_INDEX_UPPER_BOUND = 2;
	public static final long EMP_TEST_INDEX_LOWER_BOUND = 1;
	public static final long EMP_TEST_INDEX_UPPER_BOUND = 2;
	public static final int DEP_TEST_SIZE = Long.valueOf(DEP_TEST_INDEX_UPPER_BOUND - DEP_TEST_INDEX_LOWER_BOUND + 1)
			.intValue();
	public static final int EMP_TEST_SIZE = Long.valueOf(EMP_TEST_INDEX_UPPER_BOUND - EMP_TEST_INDEX_LOWER_BOUND + 1)
			.intValue();;

	public static final long EXPECTED_DEPARTMENT_ID = DEP_TEST_INDEX_LOWER_BOUND;
	public static final String EXPECTED_DEPARTMENT_NAME = Constants.DEP_NAME_FUN.apply(DEP_TEST_INDEX_LOWER_BOUND);

	public static final long EXPECTED_EMPLOYEE_ID = Constants.EMP_INDEX_FUN.applyAsLong(DEP_TEST_INDEX_LOWER_BOUND,
			EMP_TEST_INDEX_LOWER_BOUND);
	public static final String EXPECTED_EMPLOYEE_F_NAME = Constants.EMP_F_NAME_FUN.apply(DEP_TEST_INDEX_LOWER_BOUND,
			EMP_TEST_INDEX_LOWER_BOUND);
	public static final String EXPECTED_EMPLOYEE_L_NAME = Constants.EMP_L_NAME_FUN.apply(DEP_TEST_INDEX_LOWER_BOUND,
			EMP_TEST_INDEX_LOWER_BOUND);

	public static final long CREATED_DEPARTMENT_ID = 12345;
	public static final String CREATED_DEPARTMENT_NAME = Constants.DEP_NAME_FUN.apply(CREATED_DEPARTMENT_ID);
	public static final String CREATED_DEPARTMENT_CONTENT = String.format("{\"id\":%d, \"name\":\"%s\"}",
			CREATED_DEPARTMENT_ID, CREATED_DEPARTMENT_NAME);
	public static final String UPDATED_DEPARTMENT_NAME = "D-Name-UPDATED";
	public static final String UPDATED_DEPARTMENT_CONTENT = String.format("{\"name\":\"%s\"}", UPDATED_DEPARTMENT_NAME);
	public static final long ABSENT_DEPARTMENT_ID = 99999;

	public static final long CREATED_EMPLOYEE_ID = 67890;
	public static final String CREATED_EMPLOYEE_F_NAME = Constants.EMP_F_NAME_FUN.apply(CREATED_DEPARTMENT_ID,
			CREATED_EMPLOYEE_ID);
	public static final String CREATED_EMPLOYEE_L_NAME = Constants.EMP_L_NAME_FUN.apply(CREATED_DEPARTMENT_ID,
			CREATED_EMPLOYEE_ID);
	public static final String CREATED_EMPLOYEE_CONTENT = String.format(
			"{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"department\":{\"id\":%d}}", CREATED_EMPLOYEE_ID,
			CREATED_EMPLOYEE_F_NAME, CREATED_EMPLOYEE_L_NAME, CREATED_DEPARTMENT_ID);
	public static final String UPDATED_EMPLOYEE_F_NAME = "EF-Name-UPDATED";
	public static final String UPDATED_EMPLOYEE_L_NAME = "EL-Name-UPDATED";
	public static final String UPDATED_EMPLOYEE_CONTENT = String.format(
			"{\"firstName\":\"%s\",\"lastName\":\"%s\",\"department\":{\"id\":%d}}", UPDATED_EMPLOYEE_F_NAME,
			UPDATED_EMPLOYEE_L_NAME, DEP_TEST_INDEX_LOWER_BOUND);
	public static final long ABSENT_EMPLOYEE_ID = 99999;

	public static final String FIND_DEP_HREF = String.format("%s%s%s", ROOT_URL, Constants.COMPANY_PATH,
			Constants.DEPARTMENTS_PATH);
	public static final String DEPARTMENTS_HREF = ROOT_URL + Constants.DEPARTMENTS_PATH + "{?page,size,sort}";

	public static final String FIND_EMP_HREF = String.format("%s%s%s", ROOT_URL, Constants.COMPANY_PATH,
			Constants.EMPLOYEES_PATH);
	public static final String FIND_EMP_BY_ID_HREF = String.format("%s/%d", FIND_EMP_HREF, EXPECTED_EMPLOYEE_ID);

	public static final String EMPLOYEES_HREF = ROOT_URL + Constants.EMPLOYEES_PATH + "{?page,size,sort}";

	public static final String NAME_END = "101";

	private TestConstants() {
		throw new IllegalStateException("Utility class");
	}
}