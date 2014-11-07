package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class GMFGraphFigureDescriptorCustomMigration extends CustomMigration {

	public Instance getToplevel(Instance handle) {
		while (handle.getContainer().instanceOf("gmfgraph.Figure")) {
			handle = handle.getContainer();
		}
		return handle;
	}

	public Instance getOrCreateDescriptor(Instance toplevel, Model model) {
		Instance descriptor = toplevel.getContainer();
		if(!descriptor.instanceOf("gmfgraph.FigureDescriptor")) {
			Instance gallery = descriptor;
			descriptor = model.newInstance("gmfgraph.FigureDescriptor");
			descriptor.set("actualFigure", toplevel);
			gallery.remove("figures", toplevel);
			gallery.add("descriptors", descriptor);
			descriptor.set("name", toplevel.get("name"));
		}
		return descriptor;
	}

	public Instance findAccess(Instance descriptor, Instance figure) {
		for (Instance access : descriptor.getLinks("accessors")) {
			if (access.get("figure") == figure) {
				return access;
			}
		}
		return null;
	}

	public Instance getOrCreateAccess(Instance descriptor, Instance handle) {
		Instance figure = null;
		if (handle.instanceOf("gmfgraph.FigureAccessor")) {
			figure = handle.getLink("typedFigure");
		} else {
			figure = handle;
		}
		Instance access = findAccess(descriptor, figure);
		if (access == null) {
			access = descriptor.getType().getModel()
					.newInstance("gmfgraph.ChildAccess");
			access.set("figure", figure);
			descriptor.add("accessors", access);
		}
		return access;
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {

		for (Instance element : model
				.getAllInstances("gmfgraph.DiagramElement")) {
			Instance handle = element.getLink("figure");
			if (handle != null) {
				Instance toplevel = getToplevel(handle);
				Instance descriptor = getOrCreateDescriptor(toplevel, model);
				element.set("figure", descriptor);
				if (toplevel != handle) {
					Instance access = getOrCreateAccess(descriptor, handle);
					if (element.instanceOf("gmfgraph.DiagramLabel")) {
						element.set("accessor", access);
					} else if (element.instanceOf("gmfgraph.Compartment")) {
						element.set("accessor", access);
					}
				}
			}
		}
	}
}
