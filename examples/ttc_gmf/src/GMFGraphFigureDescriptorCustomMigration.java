import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;

public class GMFGraphFigureDescriptorCustomMigration extends CustomMigration {

	private EReference reference;

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		reference = metamodel
				.getEReference("gmfgraph.FigureHandle.referencingElements");
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {

		for (Instance handle : model.getAllInstances("gmfgraph.FigureHandle")) {
			List<Instance> elements = handle.unset(reference);
			if (!elements.isEmpty()) {
				Instance toplevel = getToplevel(handle);
				Instance descriptor = getOrCreateDescriptor(toplevel, model);
				for (Instance element : elements) {
					element.set("figure", descriptor);
				}
				if (toplevel != handle) {
					Instance access = getOrCreateAccess(descriptor, handle);
					for (Instance element : elements) {
						if (element.instanceOf("gmfgraph.DiagramLabel")
								|| element.instanceOf("gmfgraph.Compartment")) {
							element.set("accessor", access);
						}
					}
				}
			}
		}
	}

	public Instance getToplevel(Instance handle) {
		while (handle.getContainer().instanceOf("gmfgraph.FigureHandle")) {
			handle = handle.getContainer();
		}
		return handle;
	}

	public Instance getOrCreateDescriptor(Instance toplevel, Model model) {
		Instance gallery = toplevel.getContainer();
		if (gallery.instanceOf("gmfgraph.FigureDescriptor")) {
			return gallery;
		}
		Instance descriptor = model.newInstance("gmfgraph.FigureDescriptor");
		descriptor.set("actualFigure", toplevel);
		gallery.remove("figures", toplevel);
		gallery.add("descriptors", descriptor);
		descriptor.set("name", toplevel.get("name"));
		return descriptor;
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

	public Instance findAccess(Instance descriptor, Instance figure) {
		for (Instance access : descriptor.getLinks("accessors")) {
			if (access.get("figure") == figure) {
				return access;
			}
		}
		return null;
	}
}
