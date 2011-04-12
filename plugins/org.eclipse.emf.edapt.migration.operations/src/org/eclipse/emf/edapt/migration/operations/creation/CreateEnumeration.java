package org.eclipse.emf.edapt.migration.operations.creation;

import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edapt.common.MetamodelUtils;
import org.eclipse.emf.edapt.declaration.incubator.Operation;
import org.eclipse.emf.edapt.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.declaration.incubator.Parameter;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 940CC38F192B7D441411AB2E8655882E
 */
@Operation(identifier = "createEnumeration", label = "Create Enumeration", description = "In the metamodel, an enumeration is created. In the model, nothing needs to be changed.")
public class CreateEnumeration extends OperationBase {

	/** {@description} */
	@Parameter(description = "The package in which the enumeration is created")
	public EPackage ePackage;

	/** {@description} */
	@Parameter(description = "The name of the new enumeration")
	public String name;

	/** {@description} */
	@Parameter(description = "The names of the literals of the new enumeration")
	public List<String> literals;

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EEnum eEnum = MetamodelUtils.newEEnum(ePackage, name);
		int i = 0;
		for (String literal : literals) {
			EEnumLiteral eLiteral = MetamodelUtils.newEEnumLiteral(eEnum,
					literal);
			eLiteral.setValue(i);
			i++;
		}
	}
}
