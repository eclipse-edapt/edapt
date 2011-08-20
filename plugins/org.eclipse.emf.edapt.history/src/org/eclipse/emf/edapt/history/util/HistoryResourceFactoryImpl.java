package org.eclipse.emf.edapt.history.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;

/**
 * Special {@link EcoreResourceFactoryImpl} for history resources. The factory
 * is registered by means of extensions in the plugin.xml.
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class HistoryResourceFactoryImpl extends EcoreResourceFactoryImpl {

	/** {@inheritDoc} */
	@Override
	public Resource createResource(URI uri) {
		XMLResource resource = (XMLResource) super.createResource(uri);
		resource.getDefaultSaveOptions().put(XMLResource.OPTION_URI_HANDLER,
				new PluginAwareURIHandlerImpl());
		return resource;
	}

	/**
	 * {@link URIHandler} that refers to objects in other plugins by the prefix
	 * "platform:/plugin" and not by a relative path, as is the standard
	 * behavior of EMF.
	 */
	private static class PluginAwareURIHandlerImpl extends URIHandlerImpl {
		/** {@inheritDoc} */
		@Override
		public URI deresolve(URI uri) {
			if (uri.isPlatform() && !uri.segment(1).equals(baseURI.segment(1))) {
				if (uri.isPlatformResource()) {
					uri = uri.replacePrefix(
							URI.createPlatformResourceURI("/", false),
							URI.createPlatformPluginURI("/", false));
				}
				return uri;
			}
			return super.deresolve(uri);
		}
	}
}
