package org.eclipse.emf.edapt.migration.operations.creation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
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
 * @levd.rating YELLOW Hash: 7CFC49448C97DE1926EE2865412F2227
 */
@Operation(label = "Create Class", description = "In the metamodel, a new class is created. Nothing is changed in the model.")
public class NewClass extends OperationBase {

	/** {@description} */
	@Parameter(description = "The package in which the class is created")
	public EPackage ePackage;

	/** {@description} */
	@Parameter(description = "The name of the new class")
	public String name;

	/** {@description} */
	@Parameter(description = "The super classes of the new class")
	public List<EClass> superClasses = new ArrayList<EClass>();

	/** {@description} */
	@Parameter(description = "Whether the class is abstract")
	public Boolean abstr = false;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		MetamodelUtils.newEClass(ePackage, name, superClasses, abstr);
	}
}
