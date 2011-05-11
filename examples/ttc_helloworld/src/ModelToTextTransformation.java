import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.ModelResource;

public class ModelToTextTransformation extends HelloWorldCustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel) {
		ModelResource resource = createResultResource(model);
		metamodel.setDefaultPackage("helloworldext");
		for (Instance greeting : model.getAllInstances("Greeting")) {
			String greetingText = greeting.getLink("greetingMessage").get(
					"text");
			Object personName = greeting.getLink("person").get("name");
			String text = greetingText + " " + personName + "!";
			saveResult(resource, text);
		}
	}
}
