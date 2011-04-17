package org.eclipse.emf.edapt.declaration.creation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: FDCAEC97E4B560A32E95704B1CF5F2B5
 */
@EdaptOperation(identifier = "newClass", label = "Create Class", description = "In the metamodel, a new class is created. Nothing is changed in the model.")
public class NewClass extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The package in which the class is created")
	public EPackage ePackage;

	/** {@description} */
	@EdaptParameter(description = "The name of the new class")
	public String name;

	/** {@description} */
	@EdaptParameter(optional = true, description = "The super classes of the new class")
	public List<EClass> superClasses = new ArrayList<EClass>();

	/** {@description} */
	@EdaptParameter(description = "Whether the class is abstract")
	public Boolean abstr = false;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		MetamodelUtils.newEClass(ePackage, name, superClasses, abstr);
	}
}