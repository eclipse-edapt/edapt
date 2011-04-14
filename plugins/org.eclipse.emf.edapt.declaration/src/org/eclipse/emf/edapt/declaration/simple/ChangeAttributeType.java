package org.eclipse.emf.edapt.declaration.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edapt.declaration.EdaptOperation;
import org.eclipse.emf.edapt.declaration.EdaptParameter;
import org.eclipse.emf.edapt.declaration.OperationBase;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;

/**
 * {@description}
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating YELLOW Hash: 7800367F71D41EB4081B0388CCF00A5A
 */
@EdaptOperation(identifier = "changeAttributeType", label = "Change Attribute Type", description = "In the metamodel, the type of an attribute is changed. In the model, the values are migrated based on EMF's default serialization.")
public class ChangeAttributeType extends OperationBase {

	/** {@description} */
	@EdaptParameter(description = "The attribute whose type is changed")
	public EAttribute attribute;

	/** {@description} */
	@EdaptParameter(description = "The new type of the attribute")
	public EDataType type;

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Metamodel metamodel, Model model) {
		// variables
		EClass eClass = attribute.getEContainingClass();

		// metamodel adaptation
		attribute.setEType(type);

		// model migration
		for (Instance instance : model.getAllInstances(eClass)) {
			if (instance.isSet(attribute)) {
				Object value = instance.get(attribute);
				if (attribute.isMany()) {
					List newValue = new ArrayList();
					for (Object v : (List) value) {
						Object nv = convert(v);
						newValue.add(nv);
					}
					instance.set(attribute, newValue);
				} else {
					Object newValue = convert(value);
					instance.set(attribute, newValue);
				}
			}
		}
	}

	/** Convert a value from the old to the new type of the attribute. */
	private Object convert(Object v) {
		EDataType oldType = attribute.getEAttributeType();
		String stringValue = EcoreUtil.convertToString(oldType, v);
		Object nv = EcoreUtil.createFromString(type, stringValue);
		return nv;
	}
}
