package org.eclipse.emf.edapt.migration.execution.incubator;

public class ClassLoaderFacade implements IClassLoader {
	
	private ClassLoader loader;

	public ClassLoaderFacade(ClassLoader loader) {
		this.loader = loader;
	}

	@Override
	public <T> Class<T> load(String name) throws ClassNotFoundException {
		return (Class<T>) loader.loadClass(name);
	}

}
