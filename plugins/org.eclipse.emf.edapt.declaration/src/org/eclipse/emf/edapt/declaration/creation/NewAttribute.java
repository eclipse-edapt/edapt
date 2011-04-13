package org.eclipse.emf.edapt.declaration.creation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 27F35ECA37C6A866C5CB34301BE094C7
 */
@EdaptOperation(identifier = "newAttribute", label = "Create Attribute", description = "In the metamodel, a new attribute is created. Nothing is changed in the model.")
public class NewAttribute extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The class in which the attribute is created")
	public EClass eClass;

	/** {@description} */
	@EdaptParameter(description = "The name of the new attribute")
	public String name;

	/** {@description} */
	@EdaptParameter(description = "The type of the new attribute")
	public EDataType type;

	/** {@description} */
	@EdaptParameter(description = "The lower bound of the new attribute")
	public int lowerBound = 0;

	/** {@description} */
	@EdaptParameter(description = "The upper bound of the new reference")
	public int upperBound = 1;

	/** {@description} */
	@EdaptParameter(description = "The default value literal", optional = true)
	public String defaultValue;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		MetamodelUtils.newEAttribute(eClass, name, type, lowerBound,
				upperBound, defaultValue);
	}
}
