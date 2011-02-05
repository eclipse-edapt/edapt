/* --------------------------------------------------------------------------------
 * revision 1.164
 * date: 2006-10-18 14:21:46 +0000;  author: dstadnik;  lines: +8 -34;
 * #161380 simplify feature model facets
 * -------------------------------------------------------------------------------- */

// class FeatureModelFacet
makeAbstract(gmfgen.FeatureModelFacet, gmfgen.FeatureLabelModelFacet)
inlineSuperClass(gmfgen.FeatureModelFacet)
generalizeReference(gmfgen.FeatureLabelModelFacet.metaFeature, genmodel.GenFeature, 1, -1)
rename(gmfgen.FeatureLabelModelFacet.metaFeature, "metaFeatures")

gmfgen.GenLinkLabel.eAnnotations[1].details[0].value = gmfgen.GenLinkLabel.eAnnotations[1].details[0].value.replace(
	"(modelFacet.oclIsTypeOf(FeatureLabelModelFacet) or modelFacet.oclIsTypeOf(CompositeFeatureLabelModelFacet))",
	"modelFacet.oclIsTypeOf(FeatureLabelModelFacet)"
)
gmfgen.FeatureLabelModelFacet.eAnnotations[0].details[0].value = gmfgen.FeatureLabelModelFacet.eAnnotations[0].details[0].value.replace(
	"an EStructuralFeature",
	"domain model attribute(s)"
)

replaceClass(
	gmfgen.CompositeFeatureLabelModelFacet,
	gmfgen.FeatureLabelModelFacet,
	[gmfgen.CompositeFeatureModelFacet.metaFeatures, gmfgen.CompositeFeatureLabelModelFacet.viewPattern, gmfgen.CompositeFeatureLabelModelFacet.editPattern],
	[gmfgen.FeatureLabelModelFacet.metaFeatures, gmfgen.FeatureLabelModelFacet.viewPattern, gmfgen.FeatureLabelModelFacet.editPattern]
)

replaceClass(
	gmfgen.CompositeFeatureModelFacet,
	gmfgen.FeatureLabelModelFacet,
	[gmfgen.CompositeFeatureModelFacet.metaFeatures],
	[gmfgen.FeatureLabelModelFacet.metaFeatures]
)
