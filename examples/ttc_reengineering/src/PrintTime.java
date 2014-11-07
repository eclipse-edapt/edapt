import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class PrintTime extends CustomMigration {

	public static long start;

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {
		long time = System.currentTimeMillis() - start;
		System.out.println(time + " ms");
	}
}
