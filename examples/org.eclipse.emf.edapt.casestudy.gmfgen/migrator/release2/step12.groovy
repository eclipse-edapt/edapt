/* --------------------------------------------------------------------------------
 * revision 1.246
 * date: 2008-05-07 13:56:02 +0000;  author: atikhomirov;  lines: +19 -31;
 * [228913] updated ValueExpression use in GMFGen to better accomodate codegen needs (items 1,2,3)
 * -------------------------------------------------------------------------------- */

/**
 * start custom coupled transaction
 */
import org.eclipse.emf.ecore.*;

// class ValueExpression
gmfgen.ValueExpression.newEReference("provider", gmfgen.GenExpressionProviderBase, 1, 1, false, gmfgen.GenExpressionProviderBase.expressions)
gmfgen.ValueExpression.provider.changeable = false
gmfgen.ValueExpression.provider.resolveProxies = false

// class GenExpressionProviderBase
gmfgen.GenExpressionProviderBase.expressions.lowerBound = 1
gmfgen.GenExpressionProviderBase.expressions.containment = true
gmfgen.GenExpressionProviderBase.expressions.eOpposite = gmfgen.ValueExpression.provider
gmfgen.GenExpressionProviderContainer.getProvider.delete()

replaceInheritanceByDelegation(gmfgen.GenFeatureValueSpec, gmfgen.ValueExpression, "value")

def container = gmfgen.GenExpressionProviderContainer.instances[0]

findProvider = {valueExpression ->
	for(provider in container.providers) {
		if(valueExpression.language == gmfgen.GenLanguage.java &&
				provider.instanceOf(gmfgen.GenJavaExpressionProvider)) {
			return provider
		}
		else if(valueExpression.language == provider.language) {
			return provider
		}
	}
	return null
}

findExpression = {valueExpression ->
	def provider = findProvider(valueExpression)
	if(provider != null) {
		for(expression in provider.expressions) {
			if(expression.body == valueExpression.body) {
				return expression
			}
		}
	}
	return null
}

containment2Association = {EReference reference ->
	
	// metamodel adaptation
	reference.containment = false
	
	// model migration
	for(instance in reference.eContainingClass.allInstances) {
		def valueExpression = instance.get(reference)
		if(valueExpression != null) {
			if(container == null) {
				valueExpression.delete()
			}
			else {
				def expression = findExpression(valueExpression)
				if(expression != valueExpression) {
					instance.set(reference, expression)
					valueExpression.delete()
				}
			}
		}
	}
}

containment2Association(gmfgen.TypeModelFacet.modelElementSelector)
containment2Association(gmfgen.GenLinkConstraints.sourceEnd)
containment2Association(gmfgen.GenLinkConstraints.targetEnd)
containment2Association(gmfgen.GenAuditRule.rule)
containment2Association(gmfgen.GenMetricRule.rule)
containment2Association(gmfgen.GenFeatureValueSpec.value)

deleteFeature(gmfgen.ValueExpression.language)

// class GenJavaExpressionProvider
gmfgen.GenJavaExpressionProvider.injectExpressionBody.eAnnotations[0].details[0].key = "documentaion"

// class GenFeatureValueSpec
gmfgen.GenFeatureValueSpec.value.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints/meta")
gmfgen.GenFeatureValueSpec.value.eAnnotations[0].newEStringToStringMapEntry("def", "context")
gmfgen.GenFeatureValueSpec.value.eAnnotations[0].newEStringToStringMapEntry("ocl", "featureSeqInitializer.elementClass")

gmfgen.GenFeatureValueSpec.eAnnotations[2].delete()
gmfgen.GenFeatureValueSpec.eAnnotations[1].delete()

gmfgen.nsURI = "http://www.eclipse.org/gmf/2008/GenModel"

/*
 * end custom coupled transaction
 */
