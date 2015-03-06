package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class GMFGraphTypedFigureCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel) {
		for (final Instance fa : model.getAllInstances("gmfgraph.FigureAccessor")) { //$NON-NLS-1$
			Instance tf = fa.getLink("typedFigure"); //$NON-NLS-1$
			if (tf == null) {
				tf = model.newInstance("gmfgraph.CustomFigure"); //$NON-NLS-1$
				tf.set("qualifiedClassName", "org.eclipse.draw2d.IFigure"); //$NON-NLS-1$ //$NON-NLS-2$
				fa.set("typedFigure", tf); //$NON-NLS-1$
			}
		}
	}
}
