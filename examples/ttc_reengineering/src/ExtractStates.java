import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.ModelResource;

public class ExtractStates extends CustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		Instance stateMachine = createStateMachine(model);
		createStates(model, stateMachine);
	}

	/** Create the state machine. */
	private Instance createStateMachine(Model model) {
		ModelResource resource = createResultResource(model);
		Instance stateMachine = model.newInstance("statemachine.StateMachine");
		resource.getRootInstances().add(stateMachine);
		return stateMachine;
	}

	/** Create the resource to store the state machine in. */
	private ModelResource createResultResource(Model model) {
		URI uri = model.getResources().get(0).getUri();
		URI resultUri = uri
				.trimSegments(1)
				.appendSegment(uri.trimFileExtension().lastSegment() + "result")
				.appendFileExtension(uri.fileExtension());
		return model.newResource(resultUri);
	}

	/** Create the states. */
	private void createStates(Model model, Instance stateMachine) {
		Instance stateClass = getStateClass(model);
		for (Instance subClass : getAllSubClasses(stateClass)) {
			if (!isAbstract(subClass)) {
				Instance state = model.newInstance("statemachine.State");
				state.set("name", subClass.get("name"));
				stateMachine.add("states", state);
				state.set("class", subClass);
			}
		}
	}

	/** Get the class that is super class of all classes representing states. */
	private Instance getStateClass(Model model) {
		for (Instance c : model.getAllInstances("classifiers.Class")) {
			if ("State".equals(c.get("name"))) {
				return c;
			}
		}
		return null;
	}

	/** Get all direct and indirect sub classes of a class. */
	private List<Instance> getAllSubClasses(Instance c) {
		List<Instance> subTypes = new ArrayList<Instance>();
		for (Instance subType : getSubClasses(c)) {
			if (!subTypes.contains(subType)) {
				subTypes.add(subType);
			}
			subTypes.addAll(getAllSubClasses(subType));
		}
		return subTypes;
	}

	/** Get all direct sub classes of a class. */
	private List<Instance> getSubClasses(Instance c) {
		List<Instance> subClasses = new ArrayList<Instance>();
		List<Instance> classifierReferences = c
				.getInverse("types.ClassifierReference.target");
		for (Instance classifierReference : classifierReferences) {
			Instance container = classifierReference.getContainer()
					.getContainer();
			if (container.instanceOf("classifiers.Class")) {
				subClasses.add(container);
			}
		}
		return subClasses;
	}

	/** Check whether a class is abstract. */
	private boolean isAbstract(Instance c) {
		for (Instance modifier : c.getLinks("annotationsAndModifiers")) {
			if (modifier.instanceOf("modifiers.Abstract")) {
				return true;
			}
		}
		return false;
	}
}
