/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BMW Car IT - Initial API and implementation
 *     Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.TODELETE;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.common.MetamodelExtent;
import org.eclipse.emf.edapt.declaration.Operation;
import org.eclipse.emf.edapt.declaration.TODELETE.OldOperationRegistry;
import org.eclipse.emf.edapt.history.Add;
import org.eclipse.emf.edapt.history.Create;
import org.eclipse.emf.edapt.history.Delete;
import org.eclipse.emf.edapt.history.MigrationChange;
import org.eclipse.emf.edapt.history.Move;
import org.eclipse.emf.edapt.history.OperationChange;
import org.eclipse.emf.edapt.history.OperationInstance;
import org.eclipse.emf.edapt.history.Remove;
import org.eclipse.emf.edapt.history.Set;
import org.eclipse.emf.edapt.history.ValueChange;
import org.eclipse.emf.edapt.history.reconstruction.Mapping;
import org.eclipse.emf.edapt.history.reconstruction.ReconstructorBase;
import org.eclipse.emf.edapt.history.util.HistorySwitch;


/**
 * Base class for reconstructors of metamodel adaptation code from the history
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class AdaptationCodeReconstructorBase extends ReconstructorBase {

	/**
	 * Used variables
	 */
	protected Map<EObject, String> variables;

	/**
	 * Mapping
	 */
	protected Mapping mapping;

	/**
	 * Extent
	 */
	protected MetamodelExtent extent;

	/**
	 * Code
	 */
	protected StringBuffer code;

	/**
	 * Switch to deal with the serialization of different changes
	 */
	protected AdaptationSwitch adaptationSwitch = new AdaptationSwitch();

	/**
	 * Helper for code generation
	 */
	private CodeGeneratorHelper coder;

	/*
	 * Initialization
	 */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(Mapping mapping, MetamodelExtent extent) {
		this.mapping = mapping;
		this.extent = extent;
		coder = new CodeGeneratorHelper(extent);
		init();
	}

	/**
	 * Initialize code buffer and variables
	 */
	protected void init() {
		code = new StringBuffer();
		variables = new HashMap<EObject, String>();
	}

	/*
	 * Variables
	 */

	/**
	 * Get the variable corresponding to an element
	 * 
	 * @param element
	 *            Element
	 * @return Variable
	 */
	protected String getVariable(EObject element) {
		EObject target = mapping.resolveTarget(element);
		return checkVariable(target);
	}

	/**
	 * Check whether a variable already exists for an element. Create one if
	 * there exists none.
	 * 
	 * @param element
	 * @return Variable
	 */
	private String checkVariable(EObject element) {
		String variable = variables.get(element);
		if (variable == null) {
			variable = createVariable(element);
			code.append(variable + " = " + coder.getAccessor(element) + "\n");
		}
		return variable;
	}

	/**
	 * Serialize the value of a {@link ValueChange}
	 * 
	 * @param valueChange
	 * @return Serialized value
	 */
	protected String getValue(ValueChange valueChange) {
		EStructuralFeature feature = valueChange.getFeature();
		if (feature instanceof EAttribute) {
			String value = valueChange.getDataValue();
			if (value == null) {
				return "null";
			}
			if ("java.lang.String".equals(feature.getEType()
					.getInstanceClassName())) {
				value = "\"" + escapeString(value) + "\"";
			}
			return value;
		} else if (feature instanceof EReference) {
			EObject value = valueChange.getReferenceValue();
			if (value == null) {
				return "null";
			}
			return getVariable(value);
		}
		return null;
	}

	/**
	 * Escape certain characters in a string
	 * 
	 * @param s
	 * @return Escaped string
	 */
	private String escapeString(String s) {
		s = s.replaceAll("\"", "\\\\\"");
		s = s.replaceAll("\n", "\\\\n");
		s = s.replaceAll("\r", "\\\\r");
		s = s.replaceAll("\t", "\\\\t");
		return s;
	}

	/**
	 * Create a new variable for an element
	 * 
	 * @param element
	 *            Element
	 * @return Variable
	 */
	protected String createVariable(EObject element) {
		String name = "";
		if (element instanceof ENamedElement) {
			ENamedElement namedElement = (ENamedElement) element;
			if (namedElement.getName() == null) {
				namedElement = (ENamedElement) mapping.resolveSource(element);
			}
			name = lowerFirst(namedElement.getName())
					+ element.eClass().getName().substring(1);
		} else {
			name = lowerFirst(element.eClass().getName());
		}
		Collection<String> values = variables.values();
		if (values.contains(name)) {
			int i = 2;
			while (values.contains(name + i)) {
				i++;
			}
			name = name + i;
		}
		variables.put(element, name);
		return name;
	}

	/**
	 * Ensure that the first character of a string is lower case
	 * 
	 * @param s
	 * @return String
	 */
	private String lowerFirst(String s) {
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	/*
	 * Cases
	 */

	/**
	 * Switch to deal with the serialization of different changes
	 */
	protected class AdaptationSwitch extends HistorySwitch<Boolean> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseMove(Move move) {
			EReference reference = move.getReference();
			if (reference.isMany()) {
				code.append(getVariable(move.getTarget()) + "."
						+ getFeatureName(reference) + ".add("
						+ getVariable(move.getElement()) + ")\n");
			} else {
				code.append(getVariable(move.getTarget()) + "."
						+ getFeatureName(reference) + " = "
						+ getVariable(move.getElement()) + "\n");
			}
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseSet(Set set) {
			EStructuralFeature feature = set.getFeature();
			code.append(getVariable(set.getElement()) + "."
					+ getFeatureName(feature) + " = " + getValue(set) + "\n");
			return true;
		}

		/**
		 * Get the name of a feature
		 */
		private String getFeatureName(EStructuralFeature feature) {
			String name = feature.getName();
			name = escapeFeatureName(name);
			return name;
		}

		/**
		 * Escape the feature name
		 */
		private String escapeFeatureName(String name) {
			if ("interface".equals(name) || "abstract".equals(name)
					|| "volatile".equals(name) || "transient".equals(name)) {
				name = "'" + name + "'";
			}
			if ("properties".equals(name)) {
				name = "_" + name;
			}
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseAdd(Add add) {
			EStructuralFeature feature = add.getFeature();
			code
					.append(getVariable(add.getElement()) + "."
							+ getFeatureName(feature) + ".add(" + getValue(add)
							+ ")\n");
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseRemove(Remove remove) {
			EStructuralFeature feature = remove.getFeature();
			code.append(getVariable(remove.getElement()) + "."
					+ getFeatureName(feature) + ".remove(" + getValue(remove)
					+ ")\n");
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseDelete(Delete delete) {
			code.append(getVariable(delete.getElement()) + ".delete()\n");
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseOperationChange(OperationChange operationChange) {
			OperationInstance operationInstance = (OperationInstance) mapping
					.copyResolveTarget(operationChange.getOperation());
			CodeGeneratorHelper coder = new CodeGeneratorHelper(extent);
			appendBefore(operationInstance, coder);
			String assembledCode = coder.assembleCode(operationInstance);
			code.append(assembledCode);
			appendAfter(operationInstance, coder);
			return true;
		}

		/**
		 * Append the operations that need to be executed before the operation
		 */
		private void appendBefore(OperationInstance operationInstance,
				CodeGeneratorHelper coder) {
			Operation operation = operationInstance.getOperation();
			List<Operation> beforeOperations = OldOperationRegistry.getInstance()
					.getBefore(operation);
			for (Operation before : beforeOperations) {
				String assembledCode = coder.assembleCode(before.getName(),
						operationInstance);
				code.append(assembledCode);
			}
		}

		/**
		 * Append the operations that need to be executed after the operation
		 */
		private void appendAfter(OperationInstance operationInstance,
				CodeGeneratorHelper coder) {
			Operation operation = operationInstance.getOperation();
			List<Operation> afterOperations = OldOperationRegistry.getInstance()
					.getAfter(operation);
			for (Operation after : afterOperations) {
				String assembledCode = coder.assembleCode(after.getName(),
						operationInstance);
				code.append(assembledCode);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseCreate(Create createChild) {
			EObject element = mapping.resolveTarget(createChild.getElement());
			String elementVariable = createVariable(element);
			if (createChild.getTarget() != null) {
				String targetVariable = getVariable(createChild.getTarget());
				code.append(elementVariable + " = " + targetVariable + ".new"
						+ element.eClass().getName() + "()\n");
			} else {
				code.append(elementVariable + " = new"
						+ element.eClass().getName() + "()\n");
			}
			for (ValueChange valueChange : createChild.getChanges()) {
				doSwitch(valueChange);
			}
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean caseMigrationChange(MigrationChange migrationChange) {
			code.append(migrationChange.getMigration());
			return true;
		}
	}
}
