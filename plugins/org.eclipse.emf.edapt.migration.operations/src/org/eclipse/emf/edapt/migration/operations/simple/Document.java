package org.eclipse.emf.edapt.migration.operations.simple;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
 * @levd.rating YELLOW Hash: 1E9A2A9B2C68C72E0A254D52998957D6
 */
@Operation(label = "Document Metamodel Element", description = "In the metamodel, a metamodel element is documented. Nothing is changed in the model.")
public class Document extends OperationBase {

	/** {@description} */
	@Parameter(description = "The metamodel element to be documented")
	public EModelElement element;

	/** {@description} */
	@Parameter(description = "The comment for documentation")
	public String documentation;

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		documentation = EcoreUtil.getAnnotation(element,
				"http://www.eclipse.org/emf/2002/GenModel", "documentation");
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EcoreUtil.setAnnotation(element,
				"http://www.eclipse.org/emf/2002/GenModel", "documentation",
				documentation);
	}
}
