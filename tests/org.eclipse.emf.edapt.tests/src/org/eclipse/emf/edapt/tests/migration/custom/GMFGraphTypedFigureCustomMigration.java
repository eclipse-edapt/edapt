package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public class GMFGraphTypedFigureCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel) {
		for(Instance fa : model.getAllInstances("gmfgraph.FigureAccessor")) {
			Instance tf = fa.getLink("typedFigure");
			if(tf == null) {
				tf = model.newInstance("gmfgraph.CustomFigure");
				tf.set("qualifiedClassName", "org.eclipse.draw2d.IFigure");
				fa.set("typedFigure", tf);
			}
		}
	}
}
