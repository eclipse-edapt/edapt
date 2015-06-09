package org.eclipse.emf.edapt.internal.common;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edapt.common.IResourceSetFactory;

/**
 * Default implementation of {@link IResourceSetFactory} returning an instance
 * of {@link ResourceSetImpl}.
 *
 * @author herrmi
 * @author $Author: hummelb $
 * @version $Rev: 18709 $
 * @levd.rating RED Rev:
 */
public class ResourceSetFactoryImpl implements IResourceSetFactory {

	/** {@inheritDoc} */
	@Override
	public ResourceSet createResourceSet() {
		return new ResourceSetImpl();
	}

}
