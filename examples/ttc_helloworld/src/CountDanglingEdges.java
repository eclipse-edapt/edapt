import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public class CountDanglingEdges extends HelloWorldCustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel) {
		int dangling = 0;
		for (Instance edge : model.getAllInstances("graph1.Edge")) {
			if (edge.get("src") == null || edge.get("trg") == null) {
				dangling++;
			}
		}
		saveResult(model, dangling);
	}
}
