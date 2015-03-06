package org.eclipse.emf.edapt.declaration.inheritance;

import org.eclipse.emf.edapt.declaration.EdaptLibrary;
import org.eclipse.emf.edapt.declaration.LibraryImplementation;

/**
 * {@description}
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: DC7BEF9ADBA02561523EC50C0D65DA0F
 */
@EdaptLibrary(label = "Inheritance Operations", description = "Inheritance " +
	"operations move features along the inheritance hierarchy.")
public class InheritanceOperations extends LibraryImplementation {

	/** Constructor. */
	@SuppressWarnings("deprecation")
	public InheritanceOperations() {
		addOperation(ExtractSubClass.class);
		addOperation(ExtractSuperClass.class);
		addOperation(ExtractSuperClass2.class);
		addOperation(ImitateSuperType.class);
		addOperation(InlineSubClass.class);
		addOperation(InlineSuperClass.class);
		addOperation(PullFeature.class);
		addOperation(PullOperation.class);
		addOperation(PushFeature.class);
		addOperation(PushOperation.class);
		addOperation(ReplaceFeature.class);
		addOperation(UseSuperClass.class);
	}
}
