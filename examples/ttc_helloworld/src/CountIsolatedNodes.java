import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class CountIsolatedNodes extends HelloWorldCustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel) {
		metamodel.setDefaultPackage("graph1");
		int isolated = 0;
		for (Instance node : model.getAllInstances("Node")) {
			if (node.getInverse("Edge.src").isEmpty()
					&& node.getInverse("Edge.trg").isEmpty()) {
				isolated++;
			}
		}
		saveResult(model, isolated);
	}
}
