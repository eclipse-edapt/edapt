package org.eclipse.emf.edapt.internal.migration.internal;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A {@link BackwardConverter} that materializes the model, i.e. maps it to a
 * registered metamodel.
 *
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: BBD8D1BDE5033D416A0AD09CE6688598
 */
public class MaterializingBackwardConverter extends BackwardConverter {

	/** {@inheritDoc} */
	@Override
	protected EClass resolveEClass(EClass eClass) {
		return (EClass) resolveEClassifier(eClass);
	}

	/** Map the reflective classifier to a registered one. */
	private EClassifier resolveEClassifier(EClassifier classifier) {
		final EPackage sourcePackage = classifier.getEPackage();
		final EPackage targetPackage = Registry.INSTANCE.getEPackage(sourcePackage
			.getNsURI());
		return targetPackage.getEClassifier(classifier.getName());
	}

	/** {@inheritDoc} */
	@Override
	protected EStructuralFeature resolveFeature(EStructuralFeature feature) {
		final EClass sourceClass = feature.getEContainingClass();
		final EClass targetClass = resolveEClass(sourceClass);
		return targetClass.getEStructuralFeature(feature.getName());
	}

	/** {@inheritDoc} */
	@Override
	protected Enumerator resolveLiteral(EEnumLiteral literal) {
		final EEnum sourceEnum = literal.getEEnum();
		final String value = sourceEnum.getEPackage().getEFactoryInstance()
			.convertToString(sourceEnum, literal);
		final EEnum targetEnum = (EEnum) resolveEClassifier(sourceEnum);
		return (Enumerator) targetEnum.getEPackage().getEFactoryInstance()
			.createFromString(targetEnum, value);
	}
}
