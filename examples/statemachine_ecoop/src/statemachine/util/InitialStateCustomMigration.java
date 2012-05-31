package statemachine.util;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.MigrationUtils;
import org.eclipse.emf.edapt.migration.Model;

public class InitialStateCustomMigration extends CustomMigration {
	
	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		for (Instance compositeState : model.getInstances(metamodel
				.getEClass("statemachine.CompositeState"))) {
			Instance state = MigrationUtils.choose(compositeState,
					compositeState.getLinks("state"), "Choose initial state");
			compositeState.set("initial", state);
		}
	}
}
