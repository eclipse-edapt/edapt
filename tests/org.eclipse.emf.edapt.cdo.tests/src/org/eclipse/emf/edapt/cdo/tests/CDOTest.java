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
		CDONet4jSession openSession = CDOTestUtil.self.openSession();
		CDOPackageRegistry packageRegistry = openSession.getPackageRegistry();
		for (CDOPackageUnit unit : packageRegistry.getPackageUnits()) {
			System.out.println(unit);
		}
		CDOTransaction t = openSession.openTransaction();
		CDOResource res = createResource(t);
		
		removeResource(t, res);
	}

	private void removeResource(CDOTransaction t, CDOResource res) {

		// Now we need a test model.
		t.getResourceSet().getResources().remove(res);

		try {
			t.commit();
		} catch (ConcurrentAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private CDOResource createResource(CDOTransaction t) {
		CDOResource testRes = t.getOrCreateResource("test");
		try {
			t.commit();
		} catch (ConcurrentAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testRes;
	}
}
