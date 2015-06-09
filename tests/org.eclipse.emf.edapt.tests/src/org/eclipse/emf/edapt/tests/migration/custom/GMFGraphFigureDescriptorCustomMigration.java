package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class GMFGraphFigureDescriptorCustomMigration extends CustomMigration {

	public Instance getToplevel(Instance handle) {
		while (handle.getContainer().instanceOf("gmfgraph.Figure")) { //$NON-NLS-1$
			handle = handle.getContainer();
		}
		return handle;
	}

	public Instance getOrCreateDescriptor(Instance toplevel, Model model) {
		Instance descriptor = toplevel.getContainer();
		if (!descriptor.instanceOf("gmfgraph.FigureDescriptor")) { //$NON-NLS-1$
			final Instance gallery = descriptor;
			descriptor = model.newInstance("gmfgraph.FigureDescriptor"); //$NON-NLS-1$
			descriptor.set("actualFigure", toplevel); //$NON-NLS-1$
			gallery.remove("figures", toplevel); //$NON-NLS-1$
			gallery.add("descriptors", descriptor); //$NON-NLS-1$
			descriptor.set("name", toplevel.get("name")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return descriptor;
	}

	public Instance findAccess(Instance descriptor, Instance figure) {
		for (final Instance access : descriptor.getLinks("accessors")) { //$NON-NLS-1$
			if (access.get("figure") == figure) { //$NON-NLS-1$
				return access;
			}
		}
		return null;
	}

	public Instance getOrCreateAccess(Instance descriptor, Instance handle) {
		Instance figure = null;
		if (handle.instanceOf("gmfgraph.FigureAccessor")) { //$NON-NLS-1$
			figure = handle.getLink("typedFigure"); //$NON-NLS-1$
		} else {
			figure = handle;
		}
		Instance access = findAccess(descriptor, figure);
		if (access == null) {
			access = descriptor.getType().getModel()
				.newInstance("gmfgraph.ChildAccess"); //$NON-NLS-1$
			access.set("figure", figure); //$NON-NLS-1$
			descriptor.add("accessors", access); //$NON-NLS-1$
		}
		return access;
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {

		for (final Instance element : model
			.getAllInstances("gmfgraph.DiagramElement")) { //$NON-NLS-1$
			final Instance handle = element.getLink("figure"); //$NON-NLS-1$
			if (handle != null) {
				final Instance toplevel = getToplevel(handle);
				final Instance descriptor = getOrCreateDescriptor(toplevel, model);
				element.set("figure", descriptor); //$NON-NLS-1$
				if (toplevel != handle) {
					final Instance access = getOrCreateAccess(descriptor, handle);
					if (element.instanceOf("gmfgraph.DiagramLabel")) { //$NON-NLS-1$
						element.set("accessor", access); //$NON-NLS-1$
					} else if (element.instanceOf("gmfgraph.Compartment")) { //$NON-NLS-1$
						element.set("accessor", access); //$NON-NLS-1$
					}
				}
			}
		}
	}
}
