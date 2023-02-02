package kp.company.controller;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kp.Constants;
import kp.SampleDataset;
import kp.company.assembler.EmployeeAssembler;
import kp.company.domain.Department;
import kp.company.domain.Employee;

/**
 * The HATEOAS RESTful web service controller for the {@link Employee}.
 *
 */
@RestController
@RequestMapping(Constants.COMPANY_PATH)
public class EmployeeController {

	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass().getName());

	private final EmployeeAssembler assembler;

	/**
	 * The constructor.
	 * 
	 * @param assembler the {@link EmployeeAssembler}
	 */
	public EmployeeController(EmployeeAssembler assembler) {
		super();
		this.assembler = assembler;
	}

	/**
	 * Creates a new {@link Employee}.
	 * 
	 * @param employeeOpt the {@link Optional} with the {@link Employee}
	 * @return the {@link ResponseEntity} with the {@link EntityModel} for the
	 *         {@link Employee}
	 */
	@PostMapping(Constants.EMPLOYEES_PATH)
	public ResponseEntity<EntityModel<Employee>> createEmployee(@RequestBody Optional<Employee> employeeOpt) {

		if (employeeOpt.isEmpty()) {
			logger.error("createEmployee(): bad request - empty request body");
			return ResponseEntity.badRequest().build();
		}
		final Optional<Department> departmentOpt = employeeOpt.map(Employee::department).map(Department::id)
				.map(SampleDataset::findDepartmentById).orElse(Optional.empty());
		if (departmentOpt.isEmpty()) {
			logger.error("createEmployee(): not found - employee has not existing department");
			return ResponseEntity.notFound().build();
		}
		final ResponseEntity<EntityModel<Employee>> responseEntity = saveEmployee(employeeOpt.get());
		logger.info("createEmployee():");
		return responseEntity;
	}

	/**
	 * Reads the {@link Employee} with given id.
	 * 
	 * @param idOpt the {@link Optional} with the {@link Employee} id
	 * @return the {@link ResponseEntity} with the {@link EntityModel} for the
	 *         {@link Employee}
	 */
	@GetMapping(Constants.EMPLOYEE_ID_PATH)
	public ResponseEntity<EntityModel<Employee>> findEmployeeById(@PathVariable("id") Optional<Long> idOpt) {

		if (idOpt.isEmpty()) {
			logger.error("findEmployeeById(): bad request - empty id parameter");
			return ResponseEntity.badRequest().build();
		}
		final ResponseEntity<EntityModel<Employee>> responseEntity = SampleDataset.findEmployeeById(idOpt.get())
				.map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
		logger.info(String.format("findEmployeeById(): employee id[%d]", idOpt.get()));
		return responseEntity;
	}

	/**
	 * Reads all {@link Employee}s.
	 * 
	 * @return the {@link ResponseEntity} with the {@link PagedModel}
	 */
	@GetMapping(Constants.EMPLOYEES_PATH)
	public ResponseEntity<CollectionModel<EntityModel<Employee>>> findAllEmployees() {

		final ResponseEntity<CollectionModel<EntityModel<Employee>>> responseEntity = SampleDataset.findAllEmployees()
				.map(assembler::toCollectionModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
		logger.info("findAllEmployees():");
		return responseEntity;
	}

	/**
	 * Updates the {@link Employee} with given id.
	 * 
	 * @param employeeOpt the {@link Optional} with the {@link Employee}
	 * @param idOpt       the {@link Optional} with the {@link Employee} id
	 * @return the {@link ResponseEntity} with the {@link EntityModel} for the
	 *         {@link Employee}
	 */
	@PatchMapping(Constants.EMPLOYEE_ID_PATH)
	public ResponseEntity<EntityModel<Employee>> updateEmployee(@RequestBody Optional<Employee> employeeOpt,
			@PathVariable("id") Optional<Long> idOpt) {

		if (employeeOpt.isEmpty() || idOpt.isEmpty()) {
			logger.error("updateEmployee(): bad request - empty request body or empty parameter");
			return ResponseEntity.badRequest().build();
		}
		final boolean departmentPresent = employeeOpt.map(Employee::department).map(Department::id)
				.map(SampleDataset::findDepartmentById).isPresent();
		if (!departmentPresent) {
			logger.error("updateEmployee(): not found - employee has not existing department");
			return ResponseEntity.notFound().build();
		}
		final ResponseEntity<EntityModel<Employee>> responseEntity = SampleDataset.findEmployeeById(idOpt.get())
				.map(emp -> new Employee(emp.id(), //
						employeeOpt.map(Employee::firstName).orElse(emp.firstName()),
						employeeOpt.map(Employee::lastName).orElse(emp.lastName()),
						employeeOpt.map(Employee::department).orElse(emp.department())))
				.map(this::saveEmployee).orElseGet(() -> ResponseEntity.notFound().build());
		logger.info(String.format("updateEmployee(): employee id[%d]", employeeOpt.get().id()));
		return responseEntity;
	}

	/**
	 * Deletes the {@link Employee} with given id.
	 * 
	 * @param idOpt the {@link Optional} with the {@link Employee} id
	 * @return the {@link ResponseEntity}
	 */
	@DeleteMapping(Constants.EMPLOYEE_ID_PATH)
	public ResponseEntity<EntityModel<Employee>> deleteEmployee(@PathVariable("id") Optional<Long> idOpt) {

		if (idOpt.isEmpty()) {
			logger.error("deleteEmployee(): bad request - empty id parameter");
			return ResponseEntity.badRequest().build();
		}
		SampleDataset.deleteEmployeeById(idOpt.get());
		final ResponseEntity<EntityModel<Employee>> responseEntity = ResponseEntity.noContent().build();
		logger.info(String.format("deleteEmployee(): employee id[%d]", idOpt.get()));
		return responseEntity;
	}

	/**
	 * Saves the {@link Employee}
	 * 
	 * @param employee the {@link Employee}
	 * @return the {@link ResponseEntity} with the {@link EntityModel} for the
	 *         {@link Employee}
	 */
	private ResponseEntity<EntityModel<Employee>> saveEmployee(Employee employee) {
		try {
			SampleDataset.saveEmployee(employee);
			final EntityModel<Employee> entityModel = assembler.toModel(employee);
			final URI uri = new URI(entityModel.getRequiredLink(IanaLinkRelations.SELF).getHref());
			return ResponseEntity.created(uri).body(entityModel);
		} catch (Exception e) {
			logger.error(
					String.format("saveEmployee(): exception[%s], employee id[%d]", e.getMessage(), employee.id()));
			return ResponseEntity.internalServerError().build();
		}
	}
}
