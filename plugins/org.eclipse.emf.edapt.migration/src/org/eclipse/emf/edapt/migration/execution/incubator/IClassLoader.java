package org.eclipse.emf.edapt.migration.execution.incubator;

public interface IClassLoader {

	<T> Class<T> load(String name) throws ClassNotFoundException;
}
