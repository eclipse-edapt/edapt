package org.eclipse.emf.edapt.declaration.simple;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.EdaptRestriction;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: DDB3F248A132B4B24DFC2E1C411FEE7F
 */
@EdaptOperation(identifier = "document", label = "Document Metamodel Element", description = "In the metamodel, a metamodel element is documented. Nothing is changed in the model.")
public class Document extends OperationBase {

	/** Source for the documentation. */
	private static final String GEN_MODEL_PACKAGE_NS_URI = "http://www.eclipse.org/emf/2002/GenModel";

	/** Key for the documentation. */
	private static final String DOCUMENTATION_KEY = "documentation";

	/** {@description} */
	@EdaptParameter(description = "The metamodel element to be documented")
	public EModelElement element;

	/** {@description} */
	@EdaptRestriction(parameter = "element")
	public List<String> checkElement(EModelElement element) {
		if (element instanceof EAnnotation
				|| element instanceof EStringToStringMapEntryImpl) {
			return Collections
					.singletonList("The element must not be an annotation.");
		}
		return Collections.emptyList();
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
