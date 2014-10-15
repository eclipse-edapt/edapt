import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;


public class MoveResult extends HelloWorldCustomMigration {

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		moveResult(model);
	}
}
