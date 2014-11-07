import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.ModelResource;

public abstract class HelloWorldCustomMigration extends CustomMigration {

	/** Create the resource in which to store the result of the transformation. */
	protected ModelResource createResultResource(Model model) {
		URI resultUri = getResultURI(model);
		return model.newResource(resultUri);
	}

	/** Change the location in which the model is stored to the result location. */
	protected void moveResult(Model model) {
		model.getResources().get(0).setUri(getResultURI(model));
	}

	/** Get the location in which the result should be stored. */
	private URI getResultURI(Model model) {
		URI uri = model.getResources().get(0).getUri();
		URI resultUri = uri
				.trimSegments(1)
				.appendSegment(uri.trimFileExtension().lastSegment() + "result")
				.appendFileExtension(uri.fileExtension());
		return resultUri;
	}

	/** Create the result resource and save a result of type integer in it. */
	protected void saveResult(Model model, int i) {
		ModelResource resource = createResultResource(model);
		Instance instance = model.newInstance("result.IntResult");
		instance.set("result", i);
		resource.getRootInstances().add(instance);
	}

	/** Save a result of type String in a resource. */
	protected void saveResult(ModelResource resource, String s) {
		Instance instance = resource.getModel().newInstance(
				"result.StringResult");
		instance.set("result", s);
		resource.getRootInstances().add(instance);
	}
}
