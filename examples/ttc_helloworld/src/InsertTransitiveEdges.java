import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

public class InsertTransitiveEdges extends Graph1CustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel) {
		moveResult(model);

		metamodel.setDefaultPackage("graph1");
		List<List<Instance>> pairs = new ArrayList<List<Instance>>();
		for (Instance n1 : model.getAllInstances("Node")) {
			for (Instance n2 : getReachable(n1)) {
				for (Instance n3 : getReachable(n2)) {
					pairs.add(Arrays.asList(n1, n3));
				}
			}
		}
		Instance graph = model.getAllInstances("Graph").get(0);
		for (List<Instance> pair : pairs) {
			Instance n1 = pair.get(0);
			Instance n3 = pair.get(1);
			if (!getReachable(n1).contains(n3)) {
				Instance edge = model.newInstance("Edge");
				edge.set("src", n1);
				edge.set("trg", n3);
				graph.add("edges", edge);
			}
		}
	}
}
