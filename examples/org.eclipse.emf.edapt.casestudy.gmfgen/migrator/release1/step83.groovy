/* --------------------------------------------------------------------------------
 * revision 1.222
 * date: 2007-05-03 09:58:55 +0000;  author: dstadnik;  lines: +8 -11;
 * refactor message format parser; introduce editor pattern
 * -------------------------------------------------------------------------------- */

// class FeatureLabelModelFacet
gmfgen.FeatureLabelModelFacet.newEAttribute("editorPattern", emf.EString, 0, 1)
gmfgen.FeatureLabelModelFacet.editorPattern.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.FeatureLabelModelFacet.editorPattern.eAnnotations[0].newEStringToStringMapEntry("documentation", "Pattern to produce text for inplace editor, depends on view method; if not specified then viewPattern should be used")

gmfgen.FeatureLabelModelFacet.editPattern.eAnnotations[0].details[0].value = "Pattern to extract values from input text, depends on edit method; if not specified then viewPattern should be used"

// class PackageNames
gmfgen.PackageNames.newEAttribute("parsersPackageName", emf.EString, 0, 1)

// class ProviderClassNames
gmfgen.ProviderClassNames.getAbstractParserQualifiedClassName.delete()
gmfgen.ProviderClassNames.getStructuralFeatureParserQualifiedClassName.delete()
gmfgen.ProviderClassNames.getStructuralFeaturesParserQualifiedClassName.delete()

deleteFeature(gmfgen.ProviderClassNames.structuralFeatureParserClassName)
deleteFeature(gmfgen.ProviderClassNames.abstractParserClassName)
deleteFeature(gmfgen.ProviderClassNames.structuralFeaturesParserClassName)
