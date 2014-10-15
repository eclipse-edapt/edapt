import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class CountCircles extends Graph1CustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel) {
		metamodel.setDefaultPackage("graph1");
		int circles = 0;
		for (Instance n1 : model.getAllInstances("Node")) {
			for (Instance n2 : getReachable(n1)) {
				if (n1 != n2) {
					for (Instance n3 : getReachable(n2)) {
						if (n2 != n3 && n1 != n3) {
							if (getReachable(n3).contains(n1)) {
								circles++;
							}
						}
					}
				}
			}
		}
		saveResult(model, circles);
	}
}
