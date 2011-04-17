package org.eclipse.emf.edapt.declaration.creation;

import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
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
 * @levd.rating YELLOW Hash: 7973FA3C738EB85F933281E71E4950C0
 */
@EdaptOperation(identifier = "createEnumeration", label = "Create Enumeration", description = "In the metamodel, an enumeration is created. In the model, nothing needs to be changed.")
public class CreateEnumeration extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(description = "The package in which the enumeration is created")
	public EPackage ePackage;

	/** {@description} */
	@EdaptParameter(description = "The name of the new enumeration")
	public String name;

	/** {@description} */
	@EdaptParameter(optional = true, description = "The names of the literals of the new enumeration")
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