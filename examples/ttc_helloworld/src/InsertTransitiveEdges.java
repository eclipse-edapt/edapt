import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public class InsertTransitiveEdges extends Graph1CustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel) {
		moveResult(model);
		
		metamodel.setDefaultPackage("graph1");
		Instance graph = model.getAllInstances("Graph").get(0);
		for (Instance n1 : model.getAllInstances("Node")) {
			for (Instance n2 : getReachable(n1)) {
				for (Instance n3 : getReachable(n2)) {
					if (!getReachable(n1).contains(n3)) {
						Instance edge = model.newInstance("Edge");
						edge.set("src", n1);
						edge.set("trg", n3);
						graph.add("edges", edge);
					}
				}
			}
		}
	}
}
