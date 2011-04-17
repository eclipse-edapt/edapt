package org.eclipse.emf.edapt.migration.execution;

import java.lang.reflect.Field;

import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.declaration.Parameter;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.ParameterInstance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MetamodelResource;
import org.eclipse.emf.edapt.migration.MigrationFactory;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.Repository;

/**
 * Helper class to convert from {@link OperationImplementation} to
 * {@link OperationInstance} and vice versa.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationInstanceConverter {

	/** Create an empty repository. */
	public static Repository createEmptyRepository(MetamodelExtent extent) {
		Repository repository = MigrationFactory.eINSTANCE.createRepository();
		Metamodel metamodel = MigrationFactory.eINSTANCE.createMetamodel();
		MetamodelResource resource = MigrationFactory.eINSTANCE
				.createMetamodelResource();
		metamodel.getResources().add(resource);
		resource.getRootPackages().addAll(extent.getRootPackages());

		Model model = MigrationFactory.eINSTANCE.createModel();
		model.setMetamodel(metamodel);
		repository.setMetamodel(metamodel);
		repository.setModel(model);
		return repository;
	}

	/** Convert an {@link OperationInstance} to an {@link OperationImplementation}. */
	public static OperationImplementation convert(OperationInstance operationInstance,
			Metamodel metamodel) {
		try {
			Operation operation = operationInstance.getOperation();
			Parameter mainParameter = operation.getMainParameter();

			OperationImplementation operationBase = (OperationImplementation) operation
					.getImplementation().newInstance();

			ParameterInstance mainParameterInstance = operationInstance
					.getParameter(mainParameter.getName());
			transfer(mainParameterInstance, operationBase);

			operationBase.initialize(metamodel);

			for (ParameterInstance parameterInstance : operationInstance
					.getParameters()) {
				if (parameterInstance != mainParameterInstance) {
					transfer(parameterInstance, operationBase);
				}
			}
			return operationBase;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Transfer the value of a {@link ParameterInstance} to an
	 * {@link OperationImplementation}.
	 */
	private static void transfer(ParameterInstance parameterInstance,
			OperationImplementation operationBase) throws NoSuchFieldException,
			IllegalAccessException {
		Field field = operationBase.getClass().getField(
				parameterInstance.getName());
		if (parameterInstance.getValue() != null) {
			field.set(operationBase, parameterInstance.getValue());
		}
	}

	/** Convert an {@link OperationImplementation} to an {@link OperationInstance}. */
	public static void convert(OperationImplementation operationBase,
			OperationInstance operationInstance) {
		try {
			for (ParameterInstance parameterInstance : operationInstance
					.getParameters()) {
				Field field = operationBase.getClass().getField(
						parameterInstance.getName());
				parameterInstance.setValue(field.get(operationBase));
			}
		} catch (Exception e) {
			// ignore exceptions
		}
	}
}