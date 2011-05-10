import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.ModelResource;

public abstract class HelloWorldCustomMigration extends CustomMigration {

	protected ModelResource createResultResource(Model model) {
		URI resultUri = getResultURI(model);
		return model.newResource(resultUri);
	}

	protected void moveResult(Model model) {
		model.getResources().get(0).setUri(getResultURI(model));
	}

	private URI getResultURI(Model model) {
		URI uri = model.getResources().get(0).getUri();
		URI resultUri = uri
				.trimSegments(1)
				.appendSegment(uri.trimFileExtension().lastSegment() + "result")
				.appendFileExtension(uri.fileExtension());
		return resultUri;
	}

	protected void saveResult(Model model, int i) {
		ModelResource resource = createResultResource(model);
		Instance instance = model.newInstance("result.IntResult");
		instance.set("result", i);
		resource.getRootInstances().add(instance);
	}

	protected void saveResult(ModelResource resource, String s) {
		Instance instance = resource.getModel().newInstance(
				"result.StringResult");
		instance.set("result", s);
		resource.getRootInstances().add(instance);
	}
}
