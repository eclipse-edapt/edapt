package org.eclipse.emf.edapt.tests.migration.custom;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.migration.Instance;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.Model;
import org.eclipse.emf.edapt.migration.execution.CustomMigration;
import org.eclipse.emf.edapt.migration.execution.MigrationException;

public class GMFGenValueExpressionCustomMigration extends CustomMigration {

	private EAttribute languageAttribute;

	private Instance container;

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
			throws MigrationException {
		model.checkConformance();
		languageAttribute = metamodel
				.getEAttribute("gmfgen.ValueExpression.language");
	}

	public Instance findProvider(Instance valueExpression) {
		Metamodel metamodel = valueExpression.getType().getModel()
				.getMetamodel();
		for (Instance provider : container.getLinks("providers")) {
			if (valueExpression.get(languageAttribute) == metamodel
					.getEEnumLiteral("gmfgen.GenLanguage.java")
					&& provider.instanceOf("gmfgen.GenJavaExpressionProvider")) {
				return provider;
			} else if (valueExpression.get(languageAttribute) == provider
					.get("language")) {
				return provider;
			}
		}
		return null;
	}

	public Instance findExpression(Instance valueExpression) {
		Instance provider = findProvider(valueExpression);
		if (provider != null) {
			for (Instance expression : provider.getLinks("expressions")) {
				if (expression.get("body").equals(valueExpression.get("body"))) {
					return expression;
				}
			}
		}
		return null;
	}

	public void containment2Association(EReference reference) {

		Model model = container.getType().getModel();
		for (Instance instance : model.getAllInstances(reference
				.getEContainingClass())) {
			Instance valueExpression = instance.get(reference);
			if (valueExpression != null) {
				if (container == null) {
					model.delete(valueExpression);
				} else {
					Instance expression = findExpression(valueExpression);
					if (expression != valueExpression) {
						instance.set(reference, expression);
						model.delete(valueExpression);
					}
				}
			}
		}
	}

	@Override
	public void migrateAfter(Model model, Metamodel metamodel)
			throws MigrationException {

		container = model.getInstances("gmfgen.GenExpressionProviderContainer")
				.get(0);

		EReference[] references = new EReference[] {
				metamodel
						.getEReference("gmfgen.TypeModelFacet.modelElementSelector"),
				metamodel.getEReference("gmfgen.GenLinkConstraints.sourceEnd"),
				metamodel.getEReference("gmfgen.GenLinkConstraints.targetEnd"),
				metamodel.getEReference("gmfgen.GenAuditRule.rule"),
				metamodel.getEReference("gmfgen.GenMetricRule.rule"),
				metamodel.getEReference("gmfgen.GenFeatureValueSpec.value") };

		for (EReference reference : references) {
			containment2Association(reference);
		}

		for (Instance instance : model
				.getAllInstances("gmfgen.ValueExpression")) {
			instance.unset(languageAttribute);
		}

		model.checkConformance();
	}
}
