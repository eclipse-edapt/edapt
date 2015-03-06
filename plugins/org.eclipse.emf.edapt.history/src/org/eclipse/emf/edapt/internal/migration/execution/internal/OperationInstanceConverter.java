package org.eclipse.emf.edapt.internal.migration.execution.internal;

import java.lang.reflect.Field;

import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.internal.common.MetamodelExtent;
import org.eclipse.emf.edapt.spi.history.OperationInstance;
import org.eclipse.emf.edapt.spi.history.ParameterInstance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.MetamodelResource;
import org.eclipse.emf.edapt.spi.migration.MigrationFactory;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.Repository;

/**
 * Helper class to convert from {@link OperationImplementation} to {@link OperationInstance} and vice versa.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationInstanceConverter {

	/** Create an empty repository. */
	public static Repository createEmptyRepository(MetamodelExtent extent) {
		final Repository repository = MigrationFactory.eINSTANCE.createRepository();
		final Metamodel metamodel = MigrationFactory.eINSTANCE.createMetamodel();
		final MetamodelResource resource = MigrationFactory.eINSTANCE
			.createMetamodelResource();
		metamodel.getResources().add(resource);
		resource.getRootPackages().addAll(extent.getRootPackages());

		final Model model = MigrationFactory.eINSTANCE.createModel();
		model.setMetamodel(metamodel);
		repository.setMetamodel(metamodel);
		repository.setModel(model);
		return repository;
	}

	/**
	 * Convert an {@link OperationInstance} to an {@link OperationImplementation}.
	 */
	public static OperationImplementation convert(
		OperationInstance operationInstance, Metamodel metamodel) {
		try {
			final Operation operation = operationInstance.getOperation();
			final Parameter mainParameter = operation.getMainParameter();

			final OperationImplementation operationImpl = (OperationImplementation) operation
				.getImplementation().newInstance();

			final ParameterInstance mainParameterInstance = operationInstance
				.getParameter(mainParameter.getName());
			transfer(mainParameterInstance, operationImpl);

			try {
				operationImpl.initialize(metamodel);
			} catch (final RuntimeException e) {
				// ignore RuntimeException to make more robust
			}

			for (final ParameterInstance parameterInstance : operationInstance
				.getParameters()) {
				if (parameterInstance != mainParameterInstance) {
					transfer(parameterInstance, operationImpl);
				}
			}
			return operationImpl;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Transfer the value of a {@link ParameterInstance} to an {@link OperationImplementation}.
	 */
	private static void transfer(ParameterInstance parameterInstance,
		OperationImplementation operationBase) throws NoSuchFieldException,
		IllegalAccessException {
		final Field field = operationBase.getClass().getField(
			parameterInstance.getName());
		if (parameterInstance.getValue() != null) {
			field.set(operationBase, parameterInstance.getValue());
		}
	}

	/**
	 * Convert an {@link OperationImplementation} to an {@link OperationInstance}.
	 */
	public static void convert(OperationImplementation operationBase,
		OperationInstance operationInstance) {
		try {
			for (final ParameterInstance parameterInstance : operationInstance
				.getParameters()) {
				final Field field = operationBase.getClass().getField(
					parameterInstance.getName());
				parameterInstance.setValue(field.get(operationBase));
			}
		} catch (final Exception e) {
			// ignore exceptions
		}
	}
}
