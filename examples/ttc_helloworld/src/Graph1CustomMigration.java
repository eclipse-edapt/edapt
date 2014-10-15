import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edapt.spi.migration.Instance;

public abstract class Graph1CustomMigration extends HelloWorldCustomMigration {

	/** Get the nodes reachable from a nodes through directed edges. */
	protected List<Instance> getReachable(Instance node) {
		List<Instance> reachable = new ArrayList<Instance>();
		for (Instance edge : node.getInverse("graph1.Edge.src")) {
			reachable.add(edge.getLink("trg"));
		}
		return reachable;
	}
}
