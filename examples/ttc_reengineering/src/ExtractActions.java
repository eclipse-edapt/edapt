import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;

public class ExtractActions extends ReengineeringCustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		for (Instance transition : model
				.getAllInstances("statemachine.Transition")) {
			Instance reference = transition.get("reference");
			transition.set("action", getAction(reference));
		}
	}

	/** Get the action of a transition. */
	private String getAction(Instance reference) {
		Instance container = getContainerOfType(reference,
				"statements.StatementListContainer");
		for (Instance statement : container.getLinks("statements")) {
			if (statement.instanceOf("statements.ExpressionStatement")) {
				Instance expression = statement.getLink("expression");
				if (expression.instanceOf("references.MethodCall")) {
					if ("send".equals(expression.getLink("target").get("name"))) {
						return expression.getLinks("arguments").get(0)
								.getLink("next").getLink("target").get("name");
					}
				}
			}
		}
		return "--";
	}
}
