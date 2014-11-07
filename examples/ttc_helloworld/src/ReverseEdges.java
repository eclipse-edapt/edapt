import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class ReverseEdges extends HelloWorldCustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel) {
		moveResult(model);
		
		for (Instance edge : model.getAllInstances("graph1.Edge")) {
			Instance src = edge.get("src");
			Instance trg = edge.get("trg");
			edge.set("src", trg);
			edge.set("trg", src);
		}
	}
}
