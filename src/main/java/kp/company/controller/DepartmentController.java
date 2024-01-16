package kp.company.controller;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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
import kp.company.assembler.DepartmentAssembler;
import kp.company.domain.Department;

/**
 * The HATEOAS RESTful web service controller for the {@link Department}.
 *
 */
@RestController
@RequestMapping(Constants.COMPANY_PATH)
public class DepartmentController {

	private static final Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass().getName());

	private final DepartmentAssembler assembler;

	/**
	 * The constructor.
	 * 
	 * @param assembler the {@link DepartmentAssembler}
	 */
	public DepartmentController(DepartmentAssembler assembler) {
		super();
		this.assembler = assembler;
	}

	/**
	 * Creates a new {@link Department}
	 * 
	 * @param departmentOpt the {@link Optional} with the {@link Department}
	 * @return the {@link ResponseEntity} with the {@link EntityModel} for the
	 *         {@link Department}
	 */
	@PostMapping(Constants.DEPARTMENTS_PATH)
	public ResponseEntity<EntityModel<Department>> createDepartment(@RequestBody Optional<Department> departmentOpt) {

		if (departmentOpt.isEmpty()) {
			logger.error("createDepartment(): bad request - empty request body");
			return ResponseEntity.badRequest().build();
		}
		final ResponseEntity<EntityModel<Department>> responseEntity = saveDepartment(departmentOpt.get());
		logger.info("createDepartment():");
		return responseEntity;
	}

	/**
	 * Reads the {@link Department} with given id.
	 *
	 * @param idOpt the {@link Optional} with the {@link Department} id
	 * @return the {@link ResponseEntity} with the {@link EntityModel} for the
	 *         {@link Department}
	 */
	@GetMapping(Constants.DEPARTMENT_ID_PATH)
	public ResponseEntity<EntityModel<Department>> findDepartmentById(@PathVariable("id") Optional<Long> idOpt) {

		if (idOpt.isEmpty()) {
			logger.error("findDepartmentById(): bad request - empty id parameter");
			return ResponseEntity.badRequest().build();
		}
		final ResponseEntity<EntityModel<Department>> responseEntity = SampleDataset.findDepartmentById(idOpt.get())
				.map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
		logger.info(String.format("findDepartmentById(): department id[%d]", idOpt.get()));
		return responseEntity;
	}

	/**
	 * Reads all {@link Department}s.
	 * 
	 * @return the {@link ResponseEntity} with the {@link CollectionModel}
	 */
	@GetMapping(Constants.DEPARTMENTS_PATH)
	public ResponseEntity<CollectionModel<EntityModel<Department>>> findAllDepartments() {

		final ResponseEntity<CollectionModel<EntityModel<Department>>> responseEntity = SampleDataset
				.findAllDepartments().map(assembler::toCollectionModel).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
		logger.info("findAllDepartments():");
		return responseEntity;
	}

	/**
	 * Updates the {@link Department} with given id.
	 * 
	 * @param departmentOpt the {@link Optional} with the {@link Department}
	 * @param idOpt         the {@link Optional} with the {@link Department} id
	 * @return the {@link ResponseEntity} with the {@link EntityModel} for the
	 *         {@link Department}
	 */
	@PatchMapping(Constants.DEPARTMENT_ID_PATH)
	public ResponseEntity<EntityModel<Department>> updateDepartment(@RequestBody Optional<Department> departmentOpt,
			@PathVariable("id") Optional<Long> idOpt) {

		if (departmentOpt.isEmpty() || idOpt.isEmpty()) {
			logger.error("updateDepartment(): bad request - empty request body or empty parameter");
			return ResponseEntity.badRequest().build();
		}
		final ResponseEntity<EntityModel<Department>> responseEntity = SampleDataset.findDepartmentById(idOpt.get())
				.map(dep -> new Department(dep.getId(), departmentOpt.get().getName(), dep.getEmployees()))
				.map(this::saveDepartment).orElseGet(() -> ResponseEntity.notFound().build());
		logger.info(String.format("updateDepartment(): department id[%d]", idOpt.get()));
		return responseEntity;
	}

	/**
	 * Deletes the {@link Department} with given id.
	 * 
	 * @param idOpt the {@link Optional} with the {@link Department} id
	 * @return the {@link ResponseEntity}
	 */
	@DeleteMapping(Constants.DEPARTMENT_ID_PATH)
	public ResponseEntity<EntityModel<Department>> deleteDepartment(@PathVariable("id") Optional<Long> idOpt) {

		if (idOpt.isEmpty()) {
			logger.error("deleteDepartment(): bad request - empty id parameter");
			return ResponseEntity.badRequest().build();
		}
		SampleDataset.deleteDepartmentById(idOpt.get());
		final ResponseEntity<EntityModel<Department>> responseEntity = ResponseEntity.noContent().build();
		logger.info(String.format("deleteDepartment(): department id[%d]", idOpt.get()));
		return responseEntity;
	}

	/**
	 * Saves the {@link Department}
	 * 
	 * @param department the {@link Department}
	 * @return the {@link ResponseEntity} with the {@link EntityModel} for the
	 *         {@link Department}
	 */
	private ResponseEntity<EntityModel<Department>> saveDepartment(Department department) {
		try {
			SampleDataset.saveDepartment(department);
			final EntityModel<Department> entityModel = assembler.toModel(department);
			final URI uri = new URI(entityModel.getRequiredLink(IanaLinkRelations.SELF).getHref());
			return ResponseEntity.created(uri).body(entityModel);
		} catch (Exception e) {
			logger.error(String.format("saveDepartment(): exception[%s], department id[%d]", e.getMessage(),
					department.getId()));
			return ResponseEntity.internalServerError().build();
		}
	}

}
