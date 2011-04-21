import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edapt.migration.*
import org.eclipse.emf.edapt.common.*



//added 4/12/08
@description("In the metamodel, an element is renamed. In the model, nothing is changed.")
@label("Rename")
renameImpl = {
	@description("The metamodel element to be renamed") ENamedElement element,
	@description("The new name") String name = element.name ->
	
	// constraints
	assert element.eContainer() == null || element.eContainer().eContents().findAll{e -> e instanceof ENamedElement}.every{n -> !n.name.equals(name)} : "The name must not be already defined by the children of the element's parent."
	
	// metamodel adaptation
	element.name = name
	if(element instanceof EEnumLiteral) {
		element.literal = name
	}
}
rename = renameImpl



// added 4/12/08 due to PCM case study
@description("In the metamodel, the type of an attribute is changed. In the model, the values are migrated based on EMF's default serialization.")
@label("Change Attribute Type")
changeAttributeTypeImpl = {
	@description("The attribute whose type is changed") EAttribute attribute,
	@description("The new type of the attribute") EDataType type ->

	// variables
	def EClass eClass = attribute.eContainingClass
	def EDataType oldType = attribute.eType
	
	// metamodel adaptation
	attribute.eType = type
	
	// model migration
	for(instance in eClass.allInstances) {
		if(instance.isSet(attribute)) {
			def value = instance.get(attribute)
			if(attribute.many) {
				def newValue = []
				for(v in value) {
					def stringValue = EcoreUtil.convertToString(oldType, v)
					def nv = EcoreUtil.createFromString(type, stringValue)
					newValues.add(nv)
				}
				instance.set(attribute, newValue)
			}
			else {
				def stringValue = EcoreUtil.convertToString(oldType, value)
				def newValue = EcoreUtil.createFromString(type, stringValue)
				instance.set(attribute, newValue)
			}
		}
	}
}
changeAttributeType = changeAttributeTypeImpl



@description("In the metamodel, a classifier is moved to a different package. In the model, nothing is changed.")
@label("Move Classifier")
moveClassifierImpl = {
	@description("The classifier to be moved") EClassifier classifier,
	@description("The package to which the classifier is moved") EPackage targetPackage ->

	// constraints
	assert classifier.ePackage != targetPackage : "The classifier must not be already part of the target package" 
	assert targetPackage.getEClassifier(classifier.name) == null : "A classifier with the same name exists in the target package"

	// metamodel adaptation
	targetPackage.eClassifiers.add(classifier)
}
moveClassifier = moveClassifierImpl



@label("Make Reference Containment")
@description("In the metamodel, a reference is made containment. In the model, its values are replaced by copies.")
makeContainmentImpl = {
	@description("The reference") EReference reference ->

	def EClass contextClass = reference.eContainingClass
	
	// constraints
	assert !reference.containment : "The reference must not already be containment."

	// metamodel adaptation
	reference.containment = true
	
	// model migration
	for(instance in contextClass.allInstances) {
		if(reference.many) {
			def values = instance.unset(reference)
			for(value in values) {
				instance.get(reference).add(value.clone())
			}
		}
		else {
			def value = instance.unset(reference)
			if(value != null) {
				instance.set(reference, value.clone())
			}
		}
	}
}
makeContainment = makeContainmentImpl



//added 18/1/2009 due to gmfgen case study
@label("Make Class Abstract")
@description("In the metamodel, a class is made abstract. In a model, instances of this class are migrated to a chosen subclass.")
makeAbstractImpl = {
	@description("The class to be made abstract") EClass eClass,
	@description("The subclass to which instances are migrated") EClass subClass ->

	// constraints
	assert !eClass.'abstract' : "The class is not yet abstract"
	assert subClass.eAllSuperTypes.contains(eClass) : "The class has to be a super type of the sub class"
	
	// metamodel adaptation
	eClass.'abstract' = true
	
	// model migration
	for(instance in eClass.instances) {
		instance.migrate(subClass)
	}
}
makeAbstract = makeAbstractImpl



//added 1/5/2009 due to the GMF case study
@label("Drop Opposite Relationship")
@description("In the metamodel, the opposite relationship between to references is dropped. In the model, nothing needs to be done.")
dropOppositeImpl = {
	@description("The reference whose opposite relationship should be dropped") EReference reference ->

	// constraints
	assert reference.eOpposite != null : "Reference must have an opposite"
	
	// metamodel adaptation
	reference.eOpposite = null
}
dropOpposite = dropOppositeImpl



@description("In the metamodel, the containment of a reference is dropped. At the same time, a new container reference is created in a container class. In the model, elements previously contained by the first reference have to be contained by the new container reference. It is assumed that these elements are indirectly contained in an instance of the container class.")
@label("Drop Containment")
dropContainmentImpl = {
	@description("The reference whose containment is dropped") EReference reference,
	@description("The container class in which the containment reference is created") EClass containerClass,
	@description("The name of the new containment reference") String containerReferenceName ->
	
	def EClass contextClass = reference.eContainingClass

	assert reference.containment : "The reference must be containment"
	
	// metamodel adaptation
	reference.containment = false
	def containerReference = containerClass.newEReference(containerReferenceName, reference.eType, 0, -1, true)
	
	// model migration
	for(contextElement in contextClass.allInstances) {
		def containerElement = contextElement;
		while(containerElement != null && !(containerElement.instanceOf(containerClass))) {
			containerElement = containerElement.getContainer()
		}
		def value = contextElement.get(reference)
		if(reference.many) {
			containerElement.get(containerReference).addAll(value)
		} else if(value != null) {
			containerElement.get(containerReference).add(value)
		}
	}
}
dropContainment = dropContainmentImpl



@label("Document Metamodel Element")
@description("In the metamodel, a metamodel element is documented. Nothing is changed in the model.")
documentImpl = {
	@description("The metamodel element to be documented") EModelElement element,
	@description("The comment for documentation") String documentation = entry.value ->

	EAnnotation annotation = element.getEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
	EObject entry = annotation != null ? annotation.details.find{d -> d.key == "documentation"} : null
	
	if(annotation == null) {
		annotation = element.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
	}
	if(entry == null) {
		entry = annotation.newEStringToStringMapEntry()
		entry.key = "documentation"
	}
	entry.value = documentation
}
document = documentImpl



//added 1/5/2009 due to the GMF case study
@label("Make Feature Volatile")
@description("In the metamodel, a feature is made volatile. In the model, its values have to be deleted.")
@deleting
makeFeatureVolatileImpl = {
	@description("The feature to be made volatile") EStructuralFeature feature,
	@description("Whether the feature is transient") boolean trans = true,
	@description("Whether the feature is derived") boolean derived = true,
	@description("Whether the feature is changeable") boolean changeable = false ->

	// constraints
	assert !feature.'volatile' : "Feature must not be volatile"
	
	// metamodel adaptation
	feature.'volatile' = true
	feature.'transient' = trans
	feature.derived = derived
	feature.changeable = changeable
	if(feature instanceof EReference && feature.eOpposite != null) {
		dropOpposite(feature)
	}

	// model migration
	for(instance in feature.eContainingClass.allInstances) {
		deleteFeatureValue(instance, feature)
	}
}
makeFeatureVolatile = makeFeatureVolatileImpl
