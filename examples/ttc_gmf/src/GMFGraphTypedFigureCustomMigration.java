import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class GMFGraphTypedFigureCustomMigration extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel) {
		for(Instance fa : model.getAllInstances("gmfgraph.FigureAccessor")) {
			Instance tf = fa.getLink("typedFigure");
			if(tf == null) {
				tf = model.newInstance("gmfgraph.CustomFigure");
				tf.set("qualifiedClassName", "org.eclipse.draw2d.IFigure");
				fa.set("typedFigure", tf);
			} else {
				tf.set("name", null);
			}
		}
	}
}
