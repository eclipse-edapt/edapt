import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edapt.migration.*
import org.eclipse.emf.edapt.common.*


@label("Create Class")
@description("In the metamodel, a new class is created. Nothing is changed in the model.")
newClassImpl = {
	@description("The package in which the class is created") EPackage ePackage,
	@description("The name of the new class") String name,
	@description("The super classes of the new class") Collection<EClass> superClasses = [],
	@description("Whether the class is abstract") boolean abstr = false ->
	
	ePackage.newEClass(name, superClasses, abstr)
}
newClass = newClassImpl



@label("Create Reference")
@description("In the metamodel, a new reference is created. Nothing is changed in the model.")
newReferenceImpl = {
	@description("The class in which the reference is created") EClass eClass,
	@description("The name of the new reference") String name, 
	@description("The type of the new reference") EClass type,
	@description("The lower bound of the new reference") int lowerBound = 0,
	@description("The upper bound of the new reference") int upperBound = 1,
	@description("Whether the new reference is a containment reference") boolean containment = false,
	@description("The opposite reference of the new reference") EReference opposite = null ->
	
	eClass.newEReference(name, type, lowerBound, upperBound, containment, opposite)
}
newReference = newReferenceImpl



@label("Create Attribute")
@description("In the metamodel, a new attribute is created. Nothing is changed in the model.")
newAttributeImpl = {
	@description("The class in which the attribute is created") EClass eClass,
	@description("The name of the new attribute") String name,
	@description("The type of the new attribute") EDataType type,
	@description("The lower bound of the new attribute") int lowerBound = 0,
	@description("The upper bound of the new reference") int upperBound = 1,
	@description("The default value literal") String defaultValue = null ->
	
	eClass.newEAttribute(name, type, lowerBound, upperBound, defaultValue)
}
newAttribute = newAttributeImpl



@description("In the metamodel, a feature is deleted. In the model, its values are deleted, too.")
@label("Delete Feature")
@deprecated
deleteFeatureImpl = {
	@description("The feature to be deleted") EStructuralFeature feature ->

	def eClass = feature.eContainingClass

	// metamodel adaptation
	feature.delete()
	
	// model migration
	for(instance in eClass.allInstances) {
		deleteFeatureValue(instance, feature)
	}
}
deleteFeature = deleteFeatureImpl



//added 6/12/08
@description("In the metamodel, a class that is no longer used is deleted. In the model, nothing is changed.")
@label("Delete Class")
deleteClassImpl = {
	@description("The class to be deleted") EClass eClass ->

	// constraints
	assert eClass.getInverse(emf.ETypedElement.eType).isEmpty() : "The class must not be the target of a reference"
	assert eClass.eSubTypes.isEmpty() : "The class must not have sub classes"
	assert eClass.eSuperTypes.isEmpty() : "The class must not have super classes"
	
	// metamodel adaptation
	eClass.delete()
}
deleteClass = deleteClassImpl



//added 19/02/2009 due to GMF case study
@label("Copy Feature")
@description("In the metamodel, a feature is copied, giving it a new name. In the model, the values are copied, accordingly.")
copyFeatureImpl = {
	@description("The feature to be copied") EStructuralFeature feature,
	@description("The name of the copy") String name ->

	def EClass contextClass = feature.eContainingClass 
	
	// metamodel adaptation
	def EStructuralFeature copiedFeature = feature.clone()
	copiedFeature.name = name
	contextClass.eStructuralFeatures.add(copiedFeature)
	if(copiedFeature instanceof EReference && copiedFeature.containment) {
		copiedFeature.containment = false
	}
	
	// model migration
	for(instance in contextClass.allInstances) {
		instance.set(copiedFeature, instance.get(feature))
	}
}
copyFeature = copyFeatureImpl



@label("Delete Package")
@description("In the metamodel, an empty package is deleted.")
deletePackageImpl = {
	@description("The package to be deleted") EPackage ePackage ->
	
	assert ePackage.eClassifiers.isEmpty() : "The package must not contain classifiers"
	assert ePackage.eSubpackages.isEmpty() : "The package must not contain subpackages"
	
	ePackage.delete()
}
deletePackage = deletePackageImpl



//added 1/5/2009 due to the GMF case study
@label("Create Opposite Reference")
@description("In the metamodel, an opposite is created for a reference. In the model, the opposite direction needs to be set.")
newOppositeReferenceImpl = {
	@description("The reference for which the opposite is created") EReference reference,
	@description("The name of the opposite reference") String name,
	@description("The lower bound of the opposite reference") int lowerBound = 0,
	@description("The upper bound of the opposite reference") int upperBound = reference.containment ? 1 : -1,
	@description("Whether the opposite reference is changeable") boolean changeable = true ->

	// variables
	def EClass eClass = reference.eType
	def EClass type = reference.eContainingClass
	
	// constraints
	assert reference.eOpposite == null : "The reference must not already have an opposite"
	assert !reference.containment || upperBound == 1 : "In case of a containment reference, the upper bound of the opposite reference must be 1."
	assert reference.containment || upperBound == -1: "In case of a cross reference, the upper bound of the opposite reference must be -1."

	// metamodel adaptation
	def opposite = eClass.newEReference(name, type, lowerBound, upperBound, false, reference)
	opposite.changeable = changeable
}
newOppositeReference = newOppositeReferenceImpl



@label("Create GMF Constraint")
@description("In the metamodel, a new constraint is introduced. Nothing is changed in the model.")
newGMFConstraintImpl = {
	@description("The metamodel element in which context the constraint is created") EModelElement element,
	@description("The OCL expression of the constraint") String ocl,
	@description("The description of the constraint") String description ->

	def annotation = element.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
	annotation.newEStringToStringMapEntry("ocl", ocl)
	annotation.newEStringToStringMapEntry("description", description)
}
newGMFConstraint = newGMFConstraintImpl



@description("In the metamodel, a feature is deleted. In the model, its values are deleted, too.")
@label("Delete Feature")
@deleting
deleteFeature2Impl = {
	@description("The feature to be deleted") EStructuralFeature feature ->

	def eClass = feature.eContainingClass
	
	// model migration
	for(instance in eClass.allInstances) {
		deleteFeatureValue(instance, feature)
	}

	// metamodel adaptation
	feature.delete()
	if(feature instanceof EReference && feature.eOpposite != null) {
		feature.eOpposite.delete()
	}
}
deleteFeature2 = deleteFeature2Impl



@description("In the metamodel, the opposite of a reference is deleted. In the model, its values are deleted, too.")
@label("Delete Opposite Reference")
deleteOppositeReferenceImpl = {
	@description("The reference whose opposite should be deleted") EStructuralFeature reference ->
	
	EReference opposite = reference.eOpposite
	
	assert opposite != null : "The reference needs to define an opposite"

	def eClass = opposite.eContainingClass

	// metamodel adaptation
	opposite.delete()
	
	// model migration
	for(instance in eClass.allInstances) {
		deleteFeatureValue(instance, opposite)
	}
}
deleteOppositeReference = deleteOppositeReferenceImpl



@description("In the metamodel, an enumeration is created. In the model, nothing needs to be changed.")
@label("Create Enumeration")
createEnumerationImpl = {
	@description("The package in which the enumeration is created") EPackage ePackage,
	@description("The name of the new enumeration") String name,
	@description("The names of the literals of the new enumeration") List<String> literals ->
	
	def eEnum = ePackage.newEEnum(name)
	int i = 0
	for(literal in literals) {
		def eLiteral = eEnum.newEEnumLiteral(literal)
		eLiteral.value = i
		i++
	}
	return eEnum
}
createEnumeration = createEnumerationImpl