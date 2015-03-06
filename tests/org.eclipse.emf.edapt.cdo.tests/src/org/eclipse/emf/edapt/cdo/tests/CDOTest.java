package org.eclipse.emf.edapt.cdo.tests;

import junit.framework.TestCase;

import org.eclipse.emf.cdo.common.model.CDOPackageRegistry;
import org.eclipse.emf.cdo.common.model.CDOPackageUnit;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;

/**
 *
 * @author Christophe Bouhier
 */
public class CDOTest extends TestCase {

	public void testCDO() {
		final CDONet4jSession openSession = CDOTestUtil.self.openSession();
		final CDOPackageRegistry packageRegistry = openSession.getPackageRegistry();
		for (final CDOPackageUnit unit : packageRegistry.getPackageUnits()) {
			System.out.println(unit);
		}
		final CDOTransaction t = openSession.openTransaction();
		final CDOResource res = createResource(t);

		removeResource(t, res);
	}

	private void removeResource(CDOTransaction t, CDOResource res) {

		// Now we need a test model.
		t.getResourceSet().getResources().remove(res);

		try {
			t.commit();
		} catch (final ConcurrentAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private CDOResource createResource(CDOTransaction t) {
		final CDOResource testRes = t.getOrCreateResource("test");
		try {
			t.commit();
		} catch (final ConcurrentAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testRes;
	}
}
