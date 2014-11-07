package library.util;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class WriterCustomMigration extends CustomMigration {

	private EAttribute authorAttribute;

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		authorAttribute = metamodel.getEAttribute("library.Book.author");
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		for (Instance book : model.getAllInstances("library.Book")) {
			String author = book.unset(authorAttribute);
			Instance library = book.getContainer();
			Instance writer = findWriter(library, author);
			// if writer with name of author cannot be found, create a new
			// writer
			if (writer == null) {
				writer = model.newInstance("library.Writer");
				writer.set("name", author);
				library.add("writers", writer);
			}
			book.set("author", writer);
		}
	}

	private Instance findWriter(Instance lib, String author) {
		for (Instance writer : lib.getLinks("writers")) {
			if (author.equals(writer.get("name"))) {
				return writer;
			}
		}
		return null;
	}
}
