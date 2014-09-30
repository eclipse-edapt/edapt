package org.eclipse.emf.edapt.migration;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.osgi.framework.Bundle;

public final class FactoryHelper {

	private static final String CLASS = "class";
	private static final String NS_URI = "nsURI";
	private static final String POINT_ID = "org.eclipse.emf.edapt.factories";

	public static final FactoryHelper INSTANCE = new FactoryHelper();

	private Map<String, Class<? extends EFactory>> nsURIToFactoryMap;

	private FactoryHelper() {
		nsURIToFactoryMap = new LinkedHashMap<String, Class<? extends EFactory>>();
		readExtensionPoint();
	}

	private void readExtensionPoint() {
		final IExtensionRegistry extensionRegistry = Platform
				.getExtensionRegistry();
		final IConfigurationElement[] configurationElements = extensionRegistry
				.getConfigurationElementsFor(POINT_ID);

		for (final IConfigurationElement configurationElement : configurationElements) {
			registerFactory(configurationElement);
		}
	}

	private void registerFactory(IConfigurationElement configurationElement) {
		try {
			String nsURI = configurationElement.getAttribute(NS_URI);
			String bundle = configurationElement.getContributor().getName();
			String className = configurationElement.getAttribute(CLASS);
			Class<? extends EFactory> clazz = loadClass(bundle, className);
			if (nsURI == null || clazz == null) {
				// TODO log?
			}
			nsURIToFactoryMap.put(nsURI, clazz);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private static Class<? extends EFactory> loadClass(String bundleName,
			String clazz) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			// TODO log
		}
		return (Class<? extends EFactory>) bundle.loadClass(clazz);

	}

	public void overrideFactory(EPackage ePackage) {
		try {
			if (!nsURIToFactoryMap.containsKey(ePackage.getNsURI())) {
				return;
			}
			Class<? extends EFactory> clazz = nsURIToFactoryMap.get(ePackage
					.getNsURI());
			EFactory eFactory = clazz.getConstructor().newInstance();
			ePackage.setEFactoryInstance(eFactory);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
