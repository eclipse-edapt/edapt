package org.eclipse.emf.edapt.tests.migration.custom;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edapt.migration.CustomMigration;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.spi.migration.Instance;
import org.eclipse.emf.edapt.spi.migration.Metamodel;
import org.eclipse.emf.edapt.spi.migration.Model;

public class GMFGenValueExpressionCustomMigration extends CustomMigration {

	private EAttribute languageAttribute;

	private Instance container;

	@Override
	public void migrateBefore(Model model, Metamodel metamodel)
		throws MigrationException {
		model.checkConformance();
		languageAttribute = metamodel
			.getEAttribute("gmfgen.ValueExpression.language"); //$NON-NLS-1$
	}

	public Instance findProvider(Instance valueExpression) {
		final Metamodel metamodel = valueExpression.getType().getModel()
			.getMetamodel();
		for (final Instance provider : container.getLinks("providers")) { //$NON-NLS-1$
			if (valueExpression.get(languageAttribute) == metamodel
				.getEEnumLiteral("gmfgen.GenLanguage.java") //$NON-NLS-1$
				&& provider.instanceOf("gmfgen.GenJavaExpressionProvider")) { //$NON-NLS-1$
				return provider;
			} else if (valueExpression.get(languageAttribute) == provider
				.get("language")) { //$NON-NLS-1$
				return provider;
			}
		}
		return null;
	}

	public Instance findExpression(Instance valueExpression) {
		final Instance provider = findProvider(valueExpression);
		if (provider != null) {
			for (final Instance expression : provider.getLinks("expressions")) { //$NON-NLS-1$
				if (expression.get("body").equals(valueExpression.get("body"))) { //$NON-NLS-1$ //$NON-NLS-2$
					return expression;
				}
			}
		}
		return null;
	}

	public void containment2Association(EReference reference, Model model) {

		for (final Instance instance : model.getAllInstances(reference
			.getEContainingClass())) {
			final Instance valueExpression = instance.get(reference);
			if (valueExpression != null) {
				if (container == null) {
					model.delete(valueExpression);
				} else {
					final Instance expression = findExpression(valueExpression);
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

		final List<Instance> containers = model
			.getInstances("gmfgen.GenExpressionProviderContainer"); //$NON-NLS-1$
		if (!containers.isEmpty()) {
			container = containers.get(0);
		}

		final EReference[] references = new EReference[] {
			metamodel
				.getEReference("gmfgen.TypeModelFacet.modelElementSelector"), //$NON-NLS-1$
			metamodel.getEReference("gmfgen.GenLinkConstraints.sourceEnd"), //$NON-NLS-1$
			metamodel.getEReference("gmfgen.GenLinkConstraints.targetEnd"), //$NON-NLS-1$
			metamodel.getEReference("gmfgen.GenAuditRule.rule"), //$NON-NLS-1$
			metamodel.getEReference("gmfgen.GenMetricRule.rule"), //$NON-NLS-1$
			metamodel.getEReference("gmfgen.GenFeatureValueSpec.value") }; //$NON-NLS-1$

		for (final EReference reference : references) {
			containment2Association(reference, model);
		}

		for (final Instance instance : model
			.getAllInstances("gmfgen.ValueExpression")) { //$NON-NLS-1$
			instance.unset(languageAttribute);
		}

		model.checkConformance();
	}
}
