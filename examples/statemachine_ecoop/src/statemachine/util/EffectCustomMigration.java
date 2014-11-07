package statemachine.util;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class EffectCustomMigration extends CustomMigration {

	private EAttribute effectAttribute;

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		effectAttribute = metamodel.getEAttribute("statemachine.State.effect");
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {

		for (Instance transition : model
				.getAllInstances("statemachine.Transition")) {
			transition.set("effect", getEffect(transition));
		}

		for (Instance state : model.getAllInstances("statemachine.State")) {
			state.unset(effectAttribute);
		}

	}

	private List<String> getEffect(Instance transition) {
		List<String> effect = new ArrayList<String>();
		Instance state = transition.get("target");
		effect.addAll(state.<List<String>> get(effectAttribute));
		while (state.instanceOf("statemachine.CompositeState")) {
			state = state.get("initial");
			effect.addAll(state.<List<String>> get(effectAttribute));
		}
		return effect;
	}
}
