package org.eclipse.emf.edapt.migration.operations.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 99C27371CB6A5B2DB1463400198F131E
 */
@Operation(label = "Create Attribute", description = "In the metamodel, a new attribute is created. Nothing is changed in the model.")
public class NewAttribute extends OperationBase {

	/** {@description} */
	@Parameter(description = "The class in which the attribute is created")
	public EClass eClass;

	/** {@description} */
	@Parameter(description = "The name of the new attribute")
	public String name;

	/** {@description} */
	@Parameter(description = "The type of the new attribute")
	public EDataType type;

	/** {@description} */
	@Parameter(description = "The lower bound of the new attribute")
	public int lowerBound;

	/** {@description} */
	@Parameter(description = "The upper bound of the new reference")
	public int upperBound;

	/** {@description} */
	@Parameter(description = "The default value literal", optional = true)
	public String defaultValue;

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		lowerBound = 0;
		lowerBound = 1;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		MetamodelUtils.newEAttribute(eClass, name, type, lowerBound,
				upperBound, defaultValue);
	}
}
