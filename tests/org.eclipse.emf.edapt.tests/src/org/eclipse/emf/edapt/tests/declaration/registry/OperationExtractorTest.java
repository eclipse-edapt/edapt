package org.eclipse.emf.edapt.tests.declaration.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edapt.declaration.DeclarationFactory;
import org.eclipse.emf.edapt.declaration.Library;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.OperationExtractor;
import org.eclipse.emf.edapt.declaration.OperationRegistry;
import org.eclipse.emf.edapt.declaration.Parameter;

/**
 * Tests for the {@link OperationExtractor}.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class OperationExtractorTest extends TestCase {

	/** Test the extraction of all registered operations. */
	public void testOperationExtraction() {
		OperationExtractor extractor = new OperationExtractor();
		Collection<Operation> operations = OperationRegistry.getInstance()
				.getOperations();
		Assert.assertEquals(71, operations.size());
		for (Operation operation : operations) {
			Class<?> c = operation.getImplementation();
			Operation extracted = extractor.extractOperation(c);
			Library library = DeclarationFactory.eINSTANCE.createLibrary();
			library.getOperations().add(extracted);

			Diagnostic diagnostic = Diagnostician.INSTANCE.validate(extracted);
			Assert.assertEquals(diagnostic.toString(), Diagnostic.OK,
					diagnostic.getSeverity());
			
			Assert.assertEquals(1, getMainParameters(extracted).size());
		}
	}

	private List<Parameter> getMainParameters(Operation operation) {
		List<Parameter> mainParameters = new ArrayList<Parameter>();
		for (Parameter parameter : operation.getParameters()) {
			if (parameter.isMain()) {
				mainParameters.add(parameter);
			}
		}
		return mainParameters;
	}
}
