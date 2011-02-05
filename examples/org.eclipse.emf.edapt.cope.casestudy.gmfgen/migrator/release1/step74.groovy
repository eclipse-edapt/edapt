/* --------------------------------------------------------------------------------
 * revision 1.213
 * date: 2007-03-16 16:09:53 +0000;  author: ashatalin;  lines: +21 -1;
 * Generating generic navigator providers for domain model.
 * -------------------------------------------------------------------------------- */

// class GenDomainModelNavigator
extractSuperClass(
	gmfgen.GenNavigator,
	[],
	gmfgen,
	"GenDomainModelNavigator"
)

gmfgen.GenDomainModelNavigator.'interface' = true

gmfgen.GenDomainModelNavigator.newEAttribute("generateDomainModelNavigator", emf.EBoolean, 0, 1)
gmfgen.GenDomainModelNavigator.newEAttribute("domainContentExtensionID", emf.EString, 0, 1)
gmfgen.GenDomainModelNavigator.newEAttribute("domainContentExtensionName", emf.EString, 0, 1)
gmfgen.GenDomainModelNavigator.newEAttribute("domainContentExtensionPriority", emf.EString, 0, 1)
gmfgen.GenDomainModelNavigator.newEAttribute("domainContentProviderClassName", emf.EString, 0, 1)
gmfgen.GenDomainModelNavigator.newEAttribute("domainLabelProviderClassName", emf.EString, 0, 1)
gmfgen.GenDomainModelNavigator.newEAttribute("domainModelElementTesterClassName", emf.EString, 0, 1)

gmfgen.GenDomainModelNavigator.newEOperation("getDomainContentProviderQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenDomainModelNavigator.generateDomainModelNavigator.defaultValueLiteral = "true"
gmfgen.GenDomainModelNavigator.newEOperation("getDomainLabelProviderQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenDomainModelNavigator.newEOperation("getDomainModelElementTesterQualifiedClassName", emf.EString, 0, 1)
