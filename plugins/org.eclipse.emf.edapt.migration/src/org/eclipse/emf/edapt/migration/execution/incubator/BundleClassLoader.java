package org.eclipse.emf.edapt.migration.execution.incubator;

import org.osgi.framework.Bundle;

public class BundleClassLoader implements IClassLoader {
	
	private Bundle bundle;

	public BundleClassLoader(Bundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public <T> Class<T> load(String name) throws ClassNotFoundException {
		return bundle.loadClass(name);
	}

}
