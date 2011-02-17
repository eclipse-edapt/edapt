import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edapt.cope.migration.*
import org.eclipse.emf.edapt.cope.common.*


@description("In the metamodel, an attribute that references elements by identifier is replaced by a reference. In the model, its values are replaced by references to that element.")
@label("Identifier to Reference")
replaceIdentifierByReferenceImpl = {
	@description("The referencing attribute") EAttribute referencingAttribute,
	@description("The referenced attribute") EAttribute referencedAttribute ->

	// variables
	def EClass referencedClass = referencedAttribute.eContainingClass
	
	// constraints
	assert referencingAttribute.eType == referencedAttribute.eType : "Referencing and referenced attribute must be of the same type"
	
	// metamodel adaptation
	def referencingClass = referencingAttribute.eContainingClass
	def referencingReference = referencingClass.newEReference(
		referencingAttribute.name,
		referencedClass,
		referencingAttribute.lowerBound,
		referencingAttribute.upperBound
	)
	referencingAttribute.delete()
	
	// model migration
	def referencedElements = [:]
	for(referencedElement in referencedClass.allInstances) {
		referencedElements[referencedElement.get(referencedAttribute)] = referencedElement
	}
	for(referencingElement in referencingClass.allInstances) {
		def reference = referencingElement.unset(referencingAttribute)
		referencingElement.set(referencingReference, referencedElements[reference])
	}
}
replaceIdentifierByReference = replaceIdentifierByReferenceImpl



@description("In the metamodel, inheritance from a super class is replaced by delegation to this class. More specifically, the super class is removed and a containment reference to this class is created. In the model, the contents associated to the super class are extracted to a separate instance of the super class.")
@label("Inheritance to Delegation")
@deleting
replaceInheritanceByDelegationImpl = {
	@description("The class from which the super class is removed") EClass subClass,
	@description("The super class to be removed") EClass superClass,
	@description("The name of the reference to the super class") String referenceName ->

	// constraints
	assert subClass.eSuperTypes.contains(superClass) : "The super class must be a super type of the sub class"
	
	// metamodel adaptation
	subClass.eSuperTypes.remove(superClass)
	def delegation = subClass.newEReference(referenceName, superClass, 1, 1, true)
	
	// model migration
	for(instance in subClass.allInstances) {
		def delegate = superClass.newInstance()
		instance.set(delegation, delegate)
		for(feature in superClass.eAllStructuralFeatures) {
			delegate.set(feature, instance.unset(feature))
		}
		for(slot in new ArrayList(instance.references)) {
			def reference = slot.eReference
			def source = slot.instance
			if(superClass == reference.eType || superClass.eAllSuperTypes.contains(reference.eType)) {
				if(reference.many) {
					source.get(reference).remove(instance)
					source.get(reference).add(delegate)
				}
				else {
					source.set(reference, delegate)
				}
				if(reference.containment) {
					instance.unset(delegation)
					instance.delete()
				}
			}
		}
	}
}
replaceInheritanceByDelegation = replaceInheritanceByDelegationImpl




//added due to Louis Rose
@description("In the metamodel, a reference is replaced by a reference class. More specifically, the reference class is now contained by the source class. In the model, links conforming to the reference are replaced by instances of the reference class.")
@label("Association to Class")
introduceReferenceClassImpl = {
	@description("The reference to be replaced by a reference class") EReference reference,
	@description("The name of the reference class") String className,
	@description("The name of the opposite reference to the source class") String sourceReferenceName = null,
	@description("The name of the opposite reference to the target class") String targetReferenceName = null ->

	// variables
	def EReference opposite = reference.eOpposite

	// constraints
	assert !reference.containment : "Reference is not allowed to be containment"
	assert opposite != null : "Reference has to have an opposite"
	assert !opposite.containment : "Opposite reference is not allowed to be containment"
	
	// metamodel adaptation
	def EClass sourceClass = reference.eContainingClass
	def EClass targetClass = reference.eType
	
	def EPackage contextPackage = sourceClass.ePackage
	def EClass referenceClass = contextPackage.newEClass(className)
	
	reference.eOpposite = null
	reference.eType = referenceClass
	opposite.eType = referenceClass
	
	reference.containment = true
		
	// model migration
	for(target in targetClass.allInstances) {
		target.unset(opposite)
	}
	for(source in sourceClass.allInstances) {
		if(reference.many) {
			for(target in source.unset(reference)) {
				def referenceInstance = referenceClass.newInstance()
				source.get(reference).add(referenceInstance)
				if(opposite.many) {
					target.get(opposite).add(referenceInstance)
				} else {
					target.set(opposite, referenceInstance)
				}
			}
		} else {
			def target = source.unset(reference)
			if(target != null) {
				def referenceInstance = referenceClass.newInstance()
				source.set(reference, referenceInstance)
				if(opposite.many) {
					target.get(opposite).add(referenceInstance)
				} else {
					target.set(opposite, referenceInstance)
				}
			}
		}
	}
	
	// metamodel adaptation
	if(sourceReferenceName != null) {
		referenceClass.newEReference(sourceReferenceName, sourceClass, 1, 1, false, reference)
	}
	if(targetReferenceName != null) {
		referenceClass.newEReference(targetReferenceName, targetClass, 1, 1, false, opposite)
	}
}
introduceReferenceClass = introduceReferenceClassImpl



@label("Sub Classes to Enumeration")
@description("In the metamodel, the subclasses of a class are replaced by an enumeration. " +
		"An enumeration with literals for all subclasses is created and an enumeration attribute is created in the class. " +
		"Finally, all subclasses are deleted, and the class is made concrete. " +
		"In the model, instances of a subclass are migrated to the class, setting the enumeration attribute to the appropriate literal.")
subClassesToEnumerationImpl = {
	@description("The context class") EClass contextClass,
	@description("The name of the enumeration attribute") String attributeName,
	@description("The package in which the enumeration is created") EPackage ePackage = contextClass.ePackage,
	@description("The name of the enumeration") String enumName ->
	
	// constraints
	assert !isConcrete(contextClass) : "The context class must be abstract"
	assert !contextClass.eSubTypes.isEmpty() : "The context class must have sub types"
	assert contextClass.eSubTypes.every{c -> c.eSubTypes.isEmpty()} : "The sub types must not have sub types again"

	// metamodel adaptation
	def enumeration = ePackage.newEEnum(enumName)
	def attribute = contextClass.newEAttribute(attributeName, enumeration, 1, 1)

	contextClass.'abstract' = false
	
	def subClasses = []
	int i = 0
	for(EClass subClass in contextClass.eSubTypes) {
		def literal = enumeration.newEEnumLiteral(subClass.name)
		literal.value = i
		subClasses.add(subClass)
		subClass.delete()
		i++
	}

	// model migration
	i = 0
	for(EClass subClass in subClasses) {
		def literal = enumeration.eLiterals[i]
		for(instance in subClass.instances) {
			instance.migrate(contextClass)
			instance.set(attribute, literal)
		}
		i++
	}
}
subClassesToEnumeration = subClassesToEnumerationImpl



@label("Enumeration to Sub Classes")
@description("In the metamodel, an enumeration attribute of a class is replaced by subclasses. " +
		"The class is made abstract, and a subclass is created for each literal of the enumeration. " +
		"The enumeration attribute is deleted and also the enumeration, if not used otherwise. " +
		"In the model, instances the class are migrated to the appropriate subclass according to the value of the enumeration attribute.")
enumerationToSubClassesImpl = {
	@description("The enumeration attribute") EAttribute enumAttribute,
	@description("The package in which the subclasses are created") EPackage ePackage = enumAttribute.eContainingClass.ePackage ->
	
	// variables
	EClass contextClass = enumAttribute.eContainingClass
	EClassifier enumeration = enumAttribute.eType
	
	// constraints
	assert isConcrete(contextClass) : "The context class must be concrete"
	assert contextClass.eSubTypes.isEmpty() : "The context class must not have sub types"
	assert enumeration instanceof EEnum : "The type of the attribute must be an enumeration"
	
	// metamodel adaptation
	def subClasses = []
	for(literal in enumeration.eLiterals) {
		def subClass = ePackage.newEClass(literal.name, [contextClass])
		subClasses.add(subClass)
	}

	enumAttribute.delete()
	if(enumeration.getInverse(emf.ETypedElement.eType).isEmpty()) {
		enumeration.delete()
	}
	
	contextClass.'abstract' = true
	
	// model migration
	for(instance in contextClass.instances) {
		def value = instance.unset(enumAttribute)
		int index = enumeration.eLiterals.indexOf(value)
		instance.migrate(subClasses[index])
	}
}
enumerationToSubClasses = enumerationToSubClassesImpl



//added 1/5/2009 due to the GMF case study
@label("Volatile to Opposite Reference")
@description("In the metamodel, a reference is changed from being volatile to an opposite. In the model, the opposite direction needs to be set.")
volatileToOppositeImpl = {
	@description("The reference which is changed from volatile to opposite") EReference reference,
	@description("The reference which is going to be the opposite") EReference opposite,
	@description("Whether the reference is going to be changeable") boolean changeable = true ->

	// constraints
	assert reference.eOpposite == null : "Reference must not already have an opposite"
	assert reference.'volatile' : "Reference must be volatile"
	assert reference.eType == opposite.eContainingClass && reference.eContainingClass == opposite.eType : "Reference and opposite must be compatible with each other"
	
	// metamodel adaptation
	reference.'volatile' = false
	reference.'transient' = false
	reference.derived = false
	reference.changeable = changeable
	opposite.eOpposite = reference
}
volatileToOpposite = volatileToOppositeImpl



//added 1/5/2009 due to the GMF case study
@label("Operation to Volatile Feature")
@description("In the metamodel, an operation is transformed into a volatile feature. In the model, nothing needs to be done.")
operationToVolatileImpl = {
	@description("The operation to be transformed") EOperation operation ->

	// constraints
	assert operation.eParameters.isEmpty() : "The operation must not have parameters"
	
	// metamodel adaptation
	def EClass eClass = operation.eContainingClass
	def EClassifier type = operation.eType
		
	def name = operation.name
	if(name.startsWith("get")) {
		name = name.substring(3)
		name = name.substring(0,1).toLowerCase() + name.substring(1)
	}
	
	def EStructuralFeature feature = null 
	if(type instanceof EClass) {
		feature = eClass.newEReference(name, type, operation.lowerBound, operation.upperBound)
	}
	else {
		feature = eClass.newEAttribute(name, type, operation.lowerBound, operation.upperBound)
	}
	feature.'volatile' = true
	feature.'transient' = true
	feature.derived = true
	feature.changeable = false
	
	operation.delete()
}
operationToVolatile = operationToVolatileImpl



@label("Not Changeable to Suppressed Set Visibility")
@description("In the metamodel, a reference is made changeable, and at the same time its setter is suppressed. Nothing is changed in the model.")
notChangeableToSuppressedSetVisibilityImpl = {
	@description("The reference to be made changeable") EReference reference ->

	reference.changeable = true
	def annotation = reference.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
	annotation.newEStringToStringMapEntry("suppressedSetVisibility", "true");
}
notChangeableToSuppressedSetVisibility = notChangeableToSuppressedSetVisibilityImpl



@label("Suppressed Set Visibility to Not Changeable")
@description("In the metamodel, the setter of a reference is made visible again, and at the same time it is made non-changeable. Nothing is changed in the model.")
suppressedSetVisibilityToNotChangeableImpl = { 
	@description("The reference whose setter is made visible again") EReference reference ->

	EAnnotation annotation = reference.eAnnotations.find{a -> a.details.find{d -> d.key == "suppressedSetVisibility"} != null}
	EObject entry = annotation.details.find{d -> d.key == "suppressedSetVisibility"}
	
	assert entry != null : "Suppressed Set Visibility must be present"
	
	reference.changeable = false
	if(annotation.details.size() > 1) {
		entry.delete()
	}
	else {
		annotation.delete()
	}
}
suppressedSetVisibilityToNotChangeable = suppressedSetVisibilityToNotChangeableImpl

