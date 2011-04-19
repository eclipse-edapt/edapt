package org.eclipse.emf.edapt.declaration.common;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.edapt.declaration.EdaptConstraint;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationImplementation;

/**
 * Base class for operations which annotate a model element.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 46CD99EC88CE8EF344F1BC738066E3D1
 */
public abstract class AnnotateModelElement extends OperationImplementation {

	/** {@description} */
	@EdaptParameter(main = true, description = "The metamodel element to be annotated")
	public EModelElement element;

	/** {@description} */
	@EdaptConstraint(restricts = "element", description = "The element must not be an annotation.")
	public boolean checkElement(EModelElement element) {
		return (!(element instanceof EAnnotation) && !(element instanceof EStringToStringMapEntryImpl));
	}
}
