package org.eclipse.emf.edapt.history.recorder;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Listener that notifies when a new resource is loaded in an existing
 * {@link ResourceSet}.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public interface IResourceLoadListener {

	/** Notify when the resource is loaded. */
	void resourceLoaded(Resource resource);
}
