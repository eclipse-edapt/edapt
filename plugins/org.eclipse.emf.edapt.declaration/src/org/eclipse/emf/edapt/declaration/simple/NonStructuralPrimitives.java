package org.eclipse.emf.edapt.declaration.simple;

import org.eclipse.emf.edapt.declaration.EdaptLibrary;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 379538381D25D78091CB00E42F964D91
 */
@EdaptLibrary(label = "Non-structural Primitives", description = "Non-structural " +
		"primitive operations modify a single, existing metamodel element, i.e. " +
		"change properties of a metamodel element.")
public class NonStructuralPrimitives extends LibraryImplementation {

	/** Constructor. */
	public NonStructuralPrimitives() {
		addOperation(ChangeAttributeType.class);
		addOperation(Document.class);
		addOperation(DropContainment.class);
		addOperation(DropOpposite.class);
		addOperation(MakeAbstract.class);
		addOperation(MakeContainment.class);
		addOperation(MakeFeatureVolatile.class);
		addOperation(MoveClassifier.class);
		addOperation(Rename.class);
	}
}
