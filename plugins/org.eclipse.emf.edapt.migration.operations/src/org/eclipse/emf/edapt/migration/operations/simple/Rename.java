package org.eclipse.emf.edapt.migration.operations.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
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
 * @levd.rating YELLOW Hash: 84284E6E3C421B27D9D6E030AFA307A5
 */
@Operation(identifier = "rename", label = "Rename", description = "In the metamodel, an element is renamed. In the model, nothing is changed.")
public class Rename extends OperationBase {

	/** {@description} */
	@Parameter(description = "The metamodel element to be renamed")
	public ENamedElement element;

	/** {@description} */
	@Parameter(description = "The new name")
	public String name;

	/** {@inheritDoc} */
	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (element.eContainer() != null) {
			for (EObject sibling : element.eContainer().eContents()) {
				if (sibling instanceof ENamedElement) {
					if (((ENamedElement) sibling).getName().equals(name)) {
						result.add("The name must not be "
								+ "already defined by the children "
								+ "of the element's parent.");
						break;
					}
				}
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (name == null) {
			name = element.getName();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// metamodel adaptation
		element.setName(name);
		if (element instanceof EEnumLiteral) {
			((EEnumLiteral) element).setLiteral(name);
		}
	}
}
