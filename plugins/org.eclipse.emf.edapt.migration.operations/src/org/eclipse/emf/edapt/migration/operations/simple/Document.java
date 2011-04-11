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
 * @levd.rating YELLOW Hash: DDB3F248A132B4B24DFC2E1C411FEE7F
 */
@Operation(label = "Document Metamodel Element", description = "In the metamodel, a metamodel element is documented. Nothing is changed in the model.")
public class Document extends OperationBase {

	/** Source for the documentation. */
	private static final String GEN_MODEL_PACKAGE_NS_URI = "http://www.eclipse.org/emf/2002/GenModel";

	/** Key for the documentation. */
	private static final String DOCUMENTATION_KEY = "documentation";

	/** {@description} */
	@Parameter(description = "The metamodel element to be documented")
	public EModelElement element;

	/** {@description} */
	@Parameter(description = "The comment for documentation")
	public String documentation;

	/** {@inheritDoc} */
	@Override
	public void initialize(Metamodel metamodel) {
		if (documentation == null) {
			documentation = EcoreUtil.getAnnotation(element,
					GEN_MODEL_PACKAGE_NS_URI, DOCUMENTATION_KEY);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void execute(Metamodel metamodel, Model model) {
		EcoreUtil.setAnnotation(element, GEN_MODEL_PACKAGE_NS_URI,
				DOCUMENTATION_KEY, documentation);
	}
}
