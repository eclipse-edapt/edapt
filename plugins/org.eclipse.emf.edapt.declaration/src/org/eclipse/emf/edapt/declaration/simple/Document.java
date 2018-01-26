package org.eclipse.emf.edapt.declaration.simple;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.common.AnnotateModelElement;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 389B8023C8F3F266FE252ED6C191B339
 */
@EdaptOperation(identifier = "document", label = "Document Metamodel Element", description = "In the metamodel, a metamodel element is documented. Nothing is changed in the model.", breaking = false)
public class Document extends AnnotateModelElement {

	/** Source for the documentation. */
	private static final String GEN_MODEL_PACKAGE_NS_URI = "http://www.eclipse.org/emf/2002/GenModel"; //$NON-NLS-1$

	/** Key for the documentation. */
	private static final String DOCUMENTATION_KEY = "documentation"; //$NON-NLS-1$

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
