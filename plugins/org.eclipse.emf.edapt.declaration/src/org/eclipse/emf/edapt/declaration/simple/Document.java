package org.eclipse.emf.edapt.declaration.simple;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
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
 * @levd.rating YELLOW Hash: 4A28B02F285BEDA17B7A0D0B745D3A22
 */
@EdaptOperation(identifier = "document", label = "Document Metamodel Element", description = "In the metamodel, a metamodel element is documented. Nothing is changed in the model.")
public class Document extends OperationImplementation {

	/** Source for the documentation. */
	private static final String GEN_MODEL_PACKAGE_NS_URI = "http://www.eclipse.org/emf/2002/GenModel";

	/** Key for the documentation. */
	private static final String DOCUMENTATION_KEY = "documentation";

	/** {@description} */
	@EdaptParameter(description = "The metamodel element to be documented")
	public EModelElement element;

	/** {@description} */
	@EdaptConstraint(restricts = "element", description = "The element must not be an annotation.")
	public boolean checkElement(EModelElement element) {
		return (!(element instanceof EAnnotation) && !(element instanceof EStringToStringMapEntryImpl));
	}

	/** {@description} */
	@EdaptParameter(description = "The comment for documentation")
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
