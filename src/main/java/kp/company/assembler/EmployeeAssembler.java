package kp.company.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import kp.Constants;
import kp.company.controller.EmployeeController;
import kp.company.domain.Employee;

/**
 * The {@link RepresentationModelAssembler} for the {@link Employee}.<br/>
 * 
 * The instance of the {@link Employee} is wrapped into the {@link EntityModel}
 * instance.<br/>
 * The collection of the {@link Employee}s is wrapped into the
 * {@link CollectionModel} instance.
 */
@Component
public class EmployeeAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

	/**
	 * The constructor.
	 */
	public EmployeeAssembler() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public EntityModel<Employee> toModel(Employee employee) {

		return EntityModel.of(employee,
				linkTo(methodOn(EmployeeController.class).findEmployeeById(Optional.of(employee.getId())))
						.withSelfRel(),
				linkTo(methodOn(EmployeeController.class).findAllEmployees())
						.withRel(Constants.EMPLOYEES_LINK_RELATION));
	}

}