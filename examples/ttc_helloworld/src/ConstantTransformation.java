import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.ModelResource;

public class ConstantTransformation extends HelloWorldCustomMigration {

	@Override
	public void migrateAfter(Model model, Metamodel metamodel) {
		ModelResource resource = createResultResource(model);

		Instance greeting = model.newInstance("helloworld.Greeting");
		greeting.set("text", "Hello World");

		resource.getRootInstances().add(greeting);
	}
}
