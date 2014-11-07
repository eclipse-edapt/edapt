import java.util.List;

import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class ExtractTransitions extends ReengineeringCustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		Instance stateMachine = model.getInstances("statemachine.StateMachine")
				.get(0);
		createTransitions(model, stateMachine);
	}

	/** Create the transitions. */
	private void createTransitions(Model model, Instance stateMachine) {
		List<Instance> states = stateMachine.getLinks("states");
		for (Instance targetState : states) {
			Instance targetClass = targetState.getLink("class");
			List<Instance> references = targetClass
					.getInverse("references.ElementReference.target");
			for (Instance reference : references) {
				if (isTransition(reference)) {
					Instance sourceClass = getContainerOfType(reference,
							"classifiers.Class");
					List<Instance> sourceStates = sourceClass
							.getInverse("statemachine.State.class");
					if (!sourceStates.isEmpty()) {
						Instance sourceState = sourceStates.get(0);
						createTransition(model, stateMachine, sourceState,
								targetState, reference);
					}
				}
			}
		}
	}

	/**
	 * Check whether a certain reference to a state class represents a
	 * transition.
	 */
	private boolean isTransition(Instance reference) {
		Instance next = reference.getLink("next");
		if (next != null && next.instanceOf("references.MethodCall")) {
			if ("Instance".equals(next.getLink("target").get("name"))) {
				next = next.get("next");
				if (next != null && next.instanceOf("references.MethodCall")) {
					if ("activate".equals(next.getLink("target").get("name"))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/** Create a transition from a source to the target state. */
	private void createTransition(Model model, Instance stateMachine,
			Instance sourceState, Instance targetState, Instance reference) {
		Instance transition = model.newInstance("statemachine.Transition");
		transition.set("src", sourceState);
		transition.set("dst", targetState);
		transition.set("reference", reference);
		stateMachine.add("transitions", transition);
	}
}
