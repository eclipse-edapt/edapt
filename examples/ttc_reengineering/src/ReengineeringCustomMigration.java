import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;


public abstract class ReengineeringCustomMigration extends CustomMigration {

	/**
	 * Get the direct or indirect container of an element that is instance of a
	 * certain type.
	 */
	protected Instance getContainerOfType(Instance instance, String type) {
		Instance container = instance;
		while (container != null && !container.instanceOf(type)) {
			container = container.getContainer();
		}
		return container;
	}
}
