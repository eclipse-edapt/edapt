package org.eclipse.emf.edapt.migration.declaration.operations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.declaration.incubator.Operation;
import org.eclipse.emf.edapt.migration.declaration.incubator.OperationBase;
import org.eclipse.emf.edapt.migration.declaration.incubator.Parameter;

@Operation(label = "Rename", description = "In the metamodel, an element is renamed. In the model, nothing is changed.")
public class Rename extends OperationBase {

	@Parameter(description = "The metamodel element to be renamed")
	public ENamedElement element;

	@Parameter(description = "The new name")
	public String name;

	@Override
	public void initialize(Metamodel metamodel) {
		if (name == null) {
			name = element.getName();
		}
	}

	@Override
	public List<String> checkCustomPreconditions(Metamodel metamodel) {
		List<String> result = new ArrayList<String>();
		if (element.eContainer() != null) {
			for (EObject sibling : element.eContainer().eContents()) {
				if (sibling instanceof ENamedElement) {
					ENamedElement namedSibling = (ENamedElement) sibling;
					if (name.equals(namedSibling.getName())) {
						result
								.add("The name must not be already defined by the children of the element's parent.");
					}
				}
			}
		}
		return result;
	}

	@Override
	public void execute(Metamodel metamodel, Model model) {
		element.setName(name);
		if (element instanceof EEnumLiteral) {
			EEnumLiteral literal = (EEnumLiteral) element;
			literal.setLiteral(name);
		}
	}
}
