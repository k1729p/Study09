package kp.company.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import kp.Constants;
import kp.company.controller.DepartmentController;
import kp.company.domain.Department;

/**
 * The {@link RepresentationModelAssembler} for the {@link Department}.<br/>
 * 
 * The instance of the {@link Department} is wrapped into the
 * {@link EntityModel} instance.<br/>
 * The collection of the {@link Department}s is wrapped into the
 * {@link CollectionModel} instance.
 */
@Component
public class DepartmentAssembler implements RepresentationModelAssembler<Department, EntityModel<Department>> {

	/**
	 * The constructor.
	 */
	public DepartmentAssembler() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public EntityModel<Department> toModel(Department department) {

		return EntityModel.of(department,
				linkTo(methodOn(DepartmentController.class).findDepartmentById(Optional.of(department.id())))
						.withSelfRel(),
				linkTo(methodOn(DepartmentController.class).findAllDepartments())
						.withRel(Constants.DEPARTMENTS_LINK_RELATION));
	}

}