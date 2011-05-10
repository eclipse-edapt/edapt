import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public class CountNodes extends HelloWorldCustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel) {
		int nodes = model.getAllInstances("graph1.Node").size();
		saveResult(model, nodes);
	}
}
