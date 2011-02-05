/* --------------------------------------------------------------------------------
 * revision 1.46
 * date: 2006-09-28 18:20:40 +0000;  author: radvorak;  lines: +62 -22;
 * #138440 Modify Feature Sequence initializer to support containment references to model elements
 * -------------------------------------------------------------------------------- */

// class FeatureInitializer
extractSuperClass(
		mappings.FeatureValueSpec,
		[mappings.FeatureValueSpec.feature, mappings.FeatureValueSpec.featureSeqInitializer],
		mappings,
		"FeatureInitializer",
		false
)
mappings.FeatureInitializer.'interface' = true

mappings.FeatureInitializer.feature.eAnnotations[2].delete()
mappings.FeatureInitializer.feature.eAnnotations[1].delete()
mappings.FeatureInitializer.feature.eAnnotations[0].delete()

mappings.FeatureInitializer.feature.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.FeatureInitializer.feature.eAnnotations[0].newEStringToStringMapEntry("documentation", "The feature for which is to be initialized by this initializer")
mappings.FeatureInitializer.feature.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.FeatureInitializer.feature.eAnnotations[1].newEStringToStringMapEntry("ocl", "feature <> null implies not featureSeqInitializer.initializers->exists(i| i <> self and feature = self.feature)")
mappings.FeatureInitializer.feature.eAnnotations[1].newEStringToStringMapEntry("description", "The feature is already initialized by another 'FeatureInitializer' in the sequence")
mappings.FeatureInitializer.feature.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.FeatureInitializer.feature.eAnnotations[2].newEStringToStringMapEntry("ocl", "feature <> null implies feature.eContainingClass.isSuperTypeOf(featureSeqInitializer.elementClass)")
mappings.FeatureInitializer.feature.eAnnotations[2].newEStringToStringMapEntry("description", "The 'feature' of 'FeatureInitializer' must be available in 'Meta Class' of the initialized element")
mappings.FeatureInitializer.feature.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.FeatureInitializer.feature.eAnnotations[3].newEStringToStringMapEntry("ocl", "feature <> null implies feature.changeable")
mappings.FeatureInitializer.feature.eAnnotations[3].newEStringToStringMapEntry("description", "The 'feature' of 'FeatureInitializer' must be changeable")

// class FeatureValueSpec
mappings.FeatureValueSpec.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints/meta")
mappings.FeatureValueSpec.eAnnotations[2].newEStringToStringMapEntry("def", "type")
mappings.FeatureValueSpec.eAnnotations[2].newEStringToStringMapEntry("ocl", "feature")

// class ReferenceNewElementSpec
mappings.newEClass("ReferenceNewElementSpec", [mappings.FeatureInitializer], false)
mappings.ReferenceNewElementSpec.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.ReferenceNewElementSpec.eAnnotations[0].newEStringToStringMapEntry("ocl", "feature.many = false implies not (newElementInitializers->size() > 1)")
mappings.ReferenceNewElementSpec.eAnnotations[0].newEStringToStringMapEntry("description", "FeatureInitializer for single element EReference can't contain multiple element initializers")
mappings.ReferenceNewElementSpec.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.ReferenceNewElementSpec.eAnnotations[1].newEStringToStringMapEntry("ocl", "let r: ecore::EReference = feature.oclAsType(ecore::EReference) in feature <> null implies r.containment")
mappings.ReferenceNewElementSpec.eAnnotations[1].newEStringToStringMapEntry("description", "'feature' of 'ReferenceNewElementSpec' must refer to containment ecore::EReference")
mappings.ReferenceNewElementSpec.newEReference("newElementInitializers", mappings.FeatureSeqInitializer, 1, -1, true, null)

// class ElementInitializer
mappings.ElementInitializer.mappingEntry.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.ElementInitializer.mappingEntry.eAnnotations[0].newEStringToStringMapEntry("documentation", "The 'MappingEntry' whose domain model element is to be intialized by this initializer")

makeFeatureVolatile(mappings.ElementInitializer.mappingEntry, true, false)

// class FeatureSeqInitializer
mappings.FeatureSeqInitializer.newEReference("elementClass", emf.EClass, 0, 1, false, null)
mappings.FeatureSeqInitializer.elementClass.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.FeatureSeqInitializer.elementClass.eAnnotations[0].newEStringToStringMapEntry("ocl", "not creatingInitializer.feature.oclIsUndefined() implies creatingInitializer.feature.oclAsType(ecore::EReference).eReferenceType.isSuperTypeOf(elementClass)")
mappings.FeatureSeqInitializer.elementClass.eAnnotations[0].newEStringToStringMapEntry("description", "'elementClass' must be the same as or sub-type of the containing 'GenReferenceNewElementSpec' reference type")
mappings.FeatureSeqInitializer.elementClass.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.FeatureSeqInitializer.elementClass.eAnnotations[1].newEStringToStringMapEntry("ocl", "not creatingInitializer.feature.oclIsUndefined() implies not (elementClass.interface or elementClass.abstract)")
mappings.FeatureSeqInitializer.elementClass.eAnnotations[1].newEStringToStringMapEntry("description", "'elementClass' must be a concrete EClass which is the same or sub-type of the containing 'GenReferenceNewElementSpec' reference type")

mappings.FeatureSeqInitializer.newEReference("creatingInitializer", mappings.ReferenceNewElementSpec, 0, 1, false, mappings.ReferenceNewElementSpec.newElementInitializers)
mappings.FeatureSeqInitializer.creatingInitializer.changeable = false

mappings.FeatureSeqInitializer.initializers.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints/meta")
mappings.FeatureSeqInitializer.initializers.eAnnotations[1].newEStringToStringMapEntry("def", "context")
mappings.FeatureSeqInitializer.initializers.eAnnotations[1].newEStringToStringMapEntry("ocl", "self.elementClass")

mappings.FeatureSeqInitializer.eAnnotations[0].details[0].value = mappings.FeatureSeqInitializer.eAnnotations[0].details[0].value + " to initialize a sequence of features"

// class MappingEntry
mappings.MappingEntry.domainInitializer.eAnnotations[1].delete()

// namespace URI
mappings.nsURI = "http://www.eclipse.org/gmf/2005/mappings/2.0"
