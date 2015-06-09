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
		metamodel.setDefaultPackage("statemachine"); //$NON-NLS-1$
		triggerAttribute = metamodel.getEAttribute("Transition.trigger"); //$NON-NLS-1$
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
		throws MigrationException {

		for (final Instance transition : model.getInstances("Transition")) { //$NON-NLS-1$
			final String triggerLabel = transition.unset(triggerAttribute);
			if (triggerLabel != null) {
				final Instance stateMachine = getStateMachine(transition);
				Instance event = stateMachine
					.evaluate("event->any (e | e.name = '" + triggerLabel //$NON-NLS-1$
						+ "')"); //$NON-NLS-1$
				if (event == null) {
					event = model.newInstance("Event"); //$NON-NLS-1$
					event.set("name", triggerLabel); //$NON-NLS-1$
					stateMachine.add("event", event); //$NON-NLS-1$
				}
				transition.set("trigger", event); //$NON-NLS-1$
			}
		}
	}

	public Instance getStateMachine(Instance transition) {
		Instance state = transition.getLink("source"); //$NON-NLS-1$
		while (state != null && !state.instanceOf("StateMachine")) { //$NON-NLS-1$
			state = state.getContainer();
		}
		return state;
	}

}
