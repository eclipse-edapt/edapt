import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class CountLoopingEdges extends HelloWorldCustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel) {
		int loops = 0;
		for (Instance edge : model.getAllInstances("graph1.Edge")) {
			if (edge.get("src") == edge.get("trg")) {
				loops++;
			}
		}
		saveResult(model, loops);
	}
}
