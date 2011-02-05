/* --------------------------------------------------------------------------------
 * revision 1.142
 * date: 2006-09-05 23:41:12 +0000;  author: radvorak;  lines: +35 -12;
 * #138440 Modify Feature Sequence initializer to support containment references to model elements
 * - updating gmfgen
 * -------------------------------------------------------------------------------- */

// class GenFeatureInitializer
extractSuperClass(
	gmfgen.GenFeatureValueSpec,
	[gmfgen.GenFeatureValueSpec.feature, gmfgen.GenFeatureValueSpec.featureSeqInitializer],
	gmfgen,
	"GenFeatureInitializer",
	false
)

gmfgen.GenFeatureInitializer.'interface' = true
gmfgen.GenFeatureInitializer.featureSeqInitializer.changeable = false

pullOperation([gmfgen.GenFeatureValueSpec.getFeatureQualifiedPackageInterfaceName], gmfgen.GenFeatureInitializer)

gmfgen.GenFeatureInitializer.feature.eAnnotations[0].details[0].value = "The feature for which is to be initialized by this initializer"
gmfgen.GenFeatureInitializer.feature.eAnnotations[1].details[1].value = gmfgen.GenFeatureInitializer.feature.eAnnotations[1].details[1].value.replace("GenFeatureValueSpec", "GenFeatureInitializer")

gmfgen.GenFeatureInitializer.feature.eAnnotations.add(gmfgen.GenFeatureValueSpec.eAnnotations[3])
gmfgen.GenFeatureInitializer.feature.eAnnotations[2].details[0].value = gmfgen.GenFeatureInitializer.feature.eAnnotations[2].details[0].value.replace("typeModelFacet.metaClass", "elementClass")
gmfgen.GenFeatureInitializer.feature.eAnnotations[2].details[1].value = gmfgen.GenFeatureInitializer.feature.eAnnotations[2].details[1].value.replace("GenFeatureValueSpec", "GenFeatureInitializer")
gmfgen.GenFeatureInitializer.feature.eAnnotations[2].details[1].value = gmfgen.GenFeatureInitializer.feature.eAnnotations[2].details[1].value.replace("feature", "'feature'")

// class GenReferenceNewElementSpec
gmfgen.newEClass("GenReferenceNewElementSpec", [gmfgen.GenFeatureInitializer], false)
gmfgen.GenReferenceNewElementSpec.newEReference("newElementInitializers", gmfgen.GenFeatureSeqInitializer, 1, -1, true, null)
gmfgen.GenReferenceNewElementSpec.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
gmfgen.GenReferenceNewElementSpec.eAnnotations[0].newEStringToStringMapEntry("ocl", "feature <> null implies feature.ecoreFeature.oclIsKindOf(ecore::EReference)")
gmfgen.GenReferenceNewElementSpec.eAnnotations[0].newEStringToStringMapEntry("description", "'feature' of 'GenReferenceNewElementSpec' must refer to ecore::EReference")

// class GenFeatureSeqInitializer
gmfgen.GenFeatureSeqInitializer.newEReference("creatingInitializer", gmfgen.GenReferenceNewElementSpec, 0, 1, false, gmfgen.GenReferenceNewElementSpec.newElementInitializers)
gmfgen.GenFeatureSeqInitializer.creatingInitializer.changeable = false

gmfgen.GenFeatureSeqInitializer.newEReference("elementClass", genmodel.GenClass, 0, 1, false, null)
gmfgen.GenFeatureSeqInitializer.elementClass.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
gmfgen.GenFeatureSeqInitializer.elementClass.eAnnotations[0].newEStringToStringMapEntry("ocl", "not creatingInitializer.feature.oclIsUndefined() implies creatingInitializer.feature.ecoreFeature.oclAsType(ecore::EReference).eReferenceType.isSuperTypeOf(elementClass.ecoreClass)")
gmfgen.GenFeatureSeqInitializer.elementClass.eAnnotations[0].newEStringToStringMapEntry("description", "'elementClass' must be the same as or sub-type of the containing 'GenReferenceNewElementSpec' reference type")
gmfgen.GenFeatureSeqInitializer.elementClass.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
gmfgen.GenFeatureSeqInitializer.elementClass.eAnnotations[1].newEStringToStringMapEntry("ocl", "not creatingInitializer.feature.oclIsUndefined() implies not (elementClass.ecoreClass.interface or elementClass.ecoreClass.abstract)")
gmfgen.GenFeatureSeqInitializer.elementClass.eAnnotations[1].newEStringToStringMapEntry("description", "'elementClass' must be a concrete EClass which is the same or sub-type of the containing 'GenReferenceNewElementSpec' reference type")

gmfgen.GenFeatureSeqInitializer.initializers.eAnnotations[1].details[1].value = "self.elementClass"

// class GenElementInitializer
gmfgen.GenElementInitializer.'abstract' = false
gmfgen.GenElementInitializer.'interface' = true

makeFeatureVolatile(gmfgen.GenElementInitializer.typeModelFacet, true, false)

// namespace URI
gmfgen.nsURI = "http://www.eclipse.org/gmf/2005/GenModel/2.0"
