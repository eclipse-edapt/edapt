package org.eclipse.emf.edapt.declaration.creation;

import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;
import org.eclipse.emf.edapt.internal.common.MetamodelFactory;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: ADB8637E3A95FC42136E39A8C7780D05
 */
@EdaptOperation(identifier = "createEnumeration", label = "Create Enumeration", description = "In the metamodel, an enumeration is created. In the model, nothing needs to be changed.")
public class CreateEnumeration extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The package in which the enumeration is created")
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
		final EEnum eEnum = MetamodelFactory.newEEnum(ePackage, name);
		int i = 0;
		for (final String literal : literals) {
			final EEnumLiteral eLiteral = MetamodelFactory.newEEnumLiteral(eEnum,
				literal);
			eLiteral.setValue(i);
			i++;
		}
	}
}
