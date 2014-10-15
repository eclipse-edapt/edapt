import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class ExtractTriggers extends ReengineeringCustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		for (Instance transition : model
				.getAllInstances("statemachine.Transition")) {
			Instance reference = transition.get("reference");
			transition.set("trigger", getTrigger(reference));
		}
	}

	/** Get the trigger of a transition. */
	private String getTrigger(Instance reference) {
		Instance method = getContainerOfType(reference, "members.ClassMethod");
		String methodName = method.get("name");
		if ("run".equals(methodName)) {
			Instance switchCase = getContainerOfType(reference,
					"statements.NormalSwitchCase");
			if (switchCase != null) {
				return switchCase.getLink("condition").getLink("target")
						.get("name");
			}
			Instance catchBlock = getContainerOfType(reference,
					"statements.CatchBlock");
			if (catchBlock != null) {
				return catchBlock.getLink("parameter").getLink("typeReference")
						.getLinks("classifierReferences").get(0)
						.getLink("target").get("name");
			}
		} else {
			return methodName;
		}
		return "--";
	}
}
