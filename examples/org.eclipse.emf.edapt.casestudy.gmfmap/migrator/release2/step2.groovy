/* --------------------------------------------------------------------------------
 * revision 1.56
 * date: 2008-04-17 11:58:49 +0000;  author: atikhomirov;  lines: +4 -6;
 * [227505] Replace FeatureValueSpec generalization of ValueExpression with aggregation, as it's better approach to model such a concept
 * -------------------------------------------------------------------------------- */

// class FeatureValueSpec
replaceInheritanceByDelegation(
		mappings.FeatureValueSpec,
		mappings.ValueExpression,
		"value"
)

mappings.FeatureValueSpec.eAnnotations[1].newEStringToStringMapEntry("ocl", "feature")
mappings.FeatureValueSpec.eAnnotations[1].details[0].value = "type"
mappings.FeatureValueSpec.eAnnotations[2].delete()

// class FeatureInitializer
mappings.FeatureInitializer.'interface' = false
