import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.ModelResource;

public class ConstantTransformationReferences extends HelloWorldCustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel) {
		ModelResource resource = createResultResource(model);
		
		metamodel.setDefaultPackage("helloworldext");

		Instance greeting = model.newInstance("Greeting");

		Instance message = model.newInstance("GreetingMessage");
		message.set("text", "Hello");
		greeting.set("greetingMessage", message);

		Instance person = model.newInstance("Person");
		greeting.set("person", person);
		person.set("name", "TTC Participants");

		resource.getRootInstances().add(greeting);
	}
}
