package org.eclipse.emf.edapt.cdo.migration;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.migration.ForwardConverter;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.ReferenceSlot;

public class StrategyForwardConverter extends ForwardConverter {

	Collection<EPackage> rootPackages;

	public StrategyForwardConverter(Collection<EPackage> ePackages) {
		this.rootPackages = ePackages;
	}

	@Override
	protected Instance newInstance(EObject eObject, boolean proxy) {
		
		
		// Lookup from the metamodel eClass so we have an EClass with 
		// a ECoreFactory instead of CDOFactory for objecss. 
		EObject find = find(eObject.eClass());

		if (find instanceof EClass) {
			// EClass eClass = eObject.eClass();
			Instance element = model.newInstance((EClass) find);
			mapping.put(eObject, element);
			if (proxy) {
				element.setUri(EcoreUtil.getURI(eObject));
			}
			return element;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initInstance(EObject eObject) {
		Instance element = resolve(eObject);
		EClass c = eObject.eClass();
		for (EAttribute attribute : c.getEAllAttributes()) {
			if (ignore(attribute)) {
				continue;
			}
			EObject lookupAttribute = find(attribute);
			if (lookupAttribute instanceof EAttribute) {
				// Get the lookup attribute and set it.
				if (eObject.eIsSet(attribute)) {
					Object value = eObject.eGet(attribute);
					element.set((EStructuralFeature) lookupAttribute, value);
				}
			}
		}

		for (EReference reference : c.getEAllReferences()) {
			if (ignore(reference)) {
				continue;
			}

			// Get the lookup attribute and set it.
			EObject lookupReference = find(reference);

			if (lookupReference instanceof EReference) {
				Object value = eObject.eGet(reference);
				if (reference.isMany()) {
					List<EObject> valueEObjects = (List<EObject>) value;
					int index = 0;
					for (EObject valueEObject : valueEObjects) {
						Instance valueInstance = resolve(valueEObject);
						if (reference.isUnique()
								&& ((List) element
										.get((EStructuralFeature) lookupReference))
										.contains(valueInstance)) {
							ReferenceSlot referenceSlot = (ReferenceSlot) element
									.getSlot((EStructuralFeature) lookupReference);
							try {
								referenceSlot.getValues().move(index,
										valueInstance);
								index++;
							} catch (IndexOutOfBoundsException e) {
								// ignore missing inverse link
							}
						} else {
							element.add((EStructuralFeature) lookupReference,
									index, valueInstance);
							index++;
						}
					}
				} else {
					if (value != null) {
						EObject valueEObject = (EObject) value;
						Instance valueInstance = resolve(valueEObject);
						element.set((EStructuralFeature) lookupReference,
								valueInstance);
					}
				}
			}
		}
	}

	/** Find an element in the metamodel created for migration. */
	@SuppressWarnings("unchecked")
	private EObject find(EObject sourceElement) {
		if (sourceElement == EcorePackage.eINSTANCE) {
			return sourceElement;
		}
		EObject sourceParent = sourceElement.eContainer();
		if (sourceParent == null) {
			EPackage sourcePackage = (EPackage) sourceElement;
			for (EPackage targetPackage : rootPackages) {
				if (targetPackage.getNsURI().equals(sourcePackage.getNsURI())) {
					return targetPackage;
				}
			}
			return sourcePackage;
		}
		EObject targetParent = find(sourceParent);
		if (targetParent == sourceParent) {
			return sourceElement;
		}
		EReference reference = sourceElement.eContainmentFeature();
		if (reference.isMany()) {

			// CB Doesn't work for CDO! (Not with already loaded resources,
			// turned on the option to emulate Generated packages, but provides
			// Classifiers here in different order....

			List<EObject> targetChildren = (List<EObject>) targetParent
					.eGet(reference);
			for (EObject t : targetChildren) {
				if (t instanceof ENamedElement
						&& sourceElement instanceof ENamedElement) {
					if( ((ENamedElement) t).getName().equals(
							((ENamedElement) sourceElement).getName())){
						return t;
					}
				}

			}
			
			// Fallback to using index ..
			List<EObject> sourceChildren = (List<EObject>) sourceParent
					.eGet(reference);
			int index = sourceChildren.indexOf(sourceElement);
			EObject targetElement = targetChildren.get(index);
			return targetElement;
		}
		EObject targetElement = (EObject) targetParent.eGet(reference);
		return targetElement;
	}

}
