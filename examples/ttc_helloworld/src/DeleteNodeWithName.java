import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class DeleteNodeWithName extends HelloWorldCustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		moveResult(model);

		metamodel.setDefaultPackage("graph1");
		for (Instance node : model.getAllInstances("Node")) {
			if ("n1".equals(node.get("name"))) {
				for (Instance edge : node.getInverse("Edge.src")) {
					model.delete(edge);
				}
				for (Instance edge : node.getInverse("Edge.trg")) {
					model.delete(edge);
				}
				model.delete(node);
			}
		}
	}
}
