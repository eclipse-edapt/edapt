package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class StateMachineEventCustomMigration extends CustomMigration {

	private EAttribute triggerAttribute;

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		metamodel.setDefaultPackage("statemachine");
		triggerAttribute = metamodel.getEAttribute("Transition.trigger");
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {

		for (Instance transition : model.getInstances("Transition")) {
			String triggerLabel = transition.unset(triggerAttribute);
			if (triggerLabel != null) {
				Instance stateMachine = getStateMachine(transition);
				Instance event = stateMachine
						.evaluate("event->any (e | e.name = '" + triggerLabel
								+ "')");
				if (event == null) {
					event = model.newInstance("Event");
					event.set("name", triggerLabel);
					stateMachine.add("event", event);
				}
				transition.set("trigger", event);
			}
		}
	}

	public Instance getStateMachine(Instance transition) {
		Instance state = transition.getLink("source");
		while (state != null && !state.instanceOf("StateMachine")) {
			state = state.getContainer();
		}
		return state;
	}

}
