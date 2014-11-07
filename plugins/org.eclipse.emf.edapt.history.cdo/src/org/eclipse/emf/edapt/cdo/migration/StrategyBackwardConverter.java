package org.eclipse.emf.edapt.cdo.migration;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.cdo.util.CDOFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.common.ResourceUtils;
import org.eclipse.emf.edapt.internal.migration.BackwardConverter;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Model;
import org.eclipse.emf.edapt.spi.migration.ModelResource;
import org.eclipse.emf.edapt.spi.migration.Type;

/**
 * 
 * A {@link BackwardConverter} which defines a Strategy for converting a Model
 * to a {@link Resource}.
 * 
 * 
 * @author Christophe Bouhier
 * 
 */
public class StrategyBackwardConverter extends BackwardConverter {

	private EFactory eFactory;

	/**
	 * We need to override the creation of Objects. In the base version, the
	 * creation is delegated to the ECoreFactory set on the EPackage. (By using
	 * ECoreUtil). However CDO will use a CDOFactoryImpl which creates CDO
	 * Dynamic Objects. This is not compatible with Edapt as a DynamicCDOObject
	 * will have a CDOClassInfo based on a previous EPackage. Here we create
	 * regular EMF Objects which are added to a resource.
	 * 
	 */
	@Override
	protected void createObjects(Type type) {
		EClass sourceClass = type.getEClass();
		EClass targetClass = resolveEClass(sourceClass);
		for (Instance element : type.getInstances()) {

			EFactory eFactoryInstance = targetClass.getEPackage()
					.getEFactoryInstance();
			EObject eObject = null;
			if (eFactoryInstance instanceof CDOFactory) {
				// We are a CDO Factory auch... we can't create
				// migrated model objects with a CDOFactory.

				if (eFactory == null) {
					Collection<EPackage> rootPackages = extent
							.getRootPackages();
					for (EPackage ePack : rootPackages) {
						eFactory = ePack.getEFactoryInstance();
					}
				}
				if (eFactory != null) {
					eObject = eFactory.create(targetClass);
				}
			}

			if (eObject == null) {
				eObject = EcoreUtil.create(targetClass);
			}

			if (element.isProxy()) {
				((InternalEObject) eObject).eSetProxyURI(element.getUri());
			}
			mapping.put(element, eObject);
		}
	}

	/**
	 * The target {@link URI} which represents a path.
	 */
	private List<URI> targetURI;

	/**
	 * The original metamodel extent needed to obtain a {@link EFactory factory}
	 */
	private MetamodelExtent extent;

	public StrategyBackwardConverter(MetamodelExtent extent, List<URI> list) {
		this.targetURI = list;
		this.extent = extent;
	}

	@Override
	protected ResourceSet initResources(Model model) {
		ResourceSet resourceSet = new ResourceSetImpl();

		ResourceUtils.register(model.getMetamodel().getEPackages(),
				resourceSet.getPackageRegistry());

		for (ModelResource modelResource : model.getResources()) {

			// FIXME How to map source/target URI's, should be a map right?
			URI target = targetURI.get(0);

			Resource resource = resourceSet.createResource(target);

			if (resource instanceof XMLResource) {
				XMLResource xmlResource = (XMLResource) resource;
				if (modelResource.getEncoding() != null) {
					xmlResource.setEncoding(modelResource.getEncoding());
				}
			}

			// We can only add to the resource when a conforming EPackage.
			for (Instance element : modelResource.getRootInstances()) {
				EObject resolve = resolve(element);
				resource.getContents().add(resolve);
			}
		}
		return resourceSet;
	}

}
