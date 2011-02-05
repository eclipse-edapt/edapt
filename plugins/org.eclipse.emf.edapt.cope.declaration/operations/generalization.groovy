import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edapt.cope.migration.*
import org.eclipse.emf.edapt.cope.common.*


@description("In the metamodel, either the type or the multiplicity of a reference is generalized. In the model, nothing is changed.")
@label("Generalize Reference")
generalizeReferenceImpl = {
	@description("The reference to be generalized") EReference reference,
	@description("The new type of the reference") EClass type = reference.eType,
	@description("The new lower bound of the reference") int lowerBound = reference.lowerBound,
	@description("The new upper bound of the reference") int upperBound = reference.upperBound ->

	// constraints
	assert reference.eType.eAllSuperTypes.plus(reference.eType).contains(type) : "The type must be the same or more general"
	assert lowerBound <= reference.lowerBound && (upperBound >= reference.upperBound || upperBound == -1) : "The multiplicity must be the same or more general"

	// metamodel adaptation
	if(reference.eType != type) {
		reference.eType = type
		if(reference.eOpposite != null) {
			type.eStructuralFeatures.add(reference.eOpposite)
		}
	}
	reference.lowerBound = lowerBound
	reference.upperBound = upperBound
}
generalizeReference = generalizeReferenceImpl



//added 4/12/08 due to PCM case study
@description("In the metamodel, the super type of a class is replaced by one of its sub classes. In the model, nothing is modified.")
@label("Specialize Super Type")
specializeSuperTypeImpl = {
	@description("The class whose super type is specialized") EClass eClass,
	@description("The super type which is replaced") EClass toReplace,
	@description("The sub class by which is replaced") EClass replaceBy ->

	// constraints
	assert eClass.eSuperTypes.contains(toReplace) : "The super type to be replaced must be a super type of the class"
	assert replaceBy.eAllSuperTypes.contains(toReplace) : "The replacing super type must be a sub type of the replaced super type"
	
	// metamodel adaptation
	eClass.eSuperTypes.remove(toReplace)
	eClass.eSuperTypes.add(replaceBy)
}
specializeSuperType = specializeSuperTypeImpl



//added 4/12/08 due to PCM case study
@description("In the metamodel, a super type is removed from a class that is already inherited from another super class. In the model, nothing is changed, as this super type is superfluous.")
@label("Remove Superfluous Super Type")
removeSuperfluousSuperTypeImpl = {
	@description("The class from which the super type is removed") EClass eClass,
	@description("The super type to be removed") EClass superType ->

	// variables
	def List<EClass> superTypes = eClass.eSuperTypes
	
	// constraints
	assert superTypes.contains(superType) : "The super type to be removed actually has to be a super type"
	assert superTypes.any{s -> s.eAllSuperTypes.contains(superType)} : "The super type to be removed must be subsumed by one of the other super types"
	
	// metamodel adaptation
	eClass.eSuperTypes.remove(superType)
}
removeSuperfluousSuperType = removeSuperfluousSuperTypeImpl



//added 4/12/08 due to PCM case study
@description("In the metamodel, a super type is removed from a class. In the model, the values of the features inherited from that super type (including its super types) are deleted.")
@label("Remove Super Type")
@deleting
removeSuperTypeImpl = {
	@description("The class from which the super type is removed") EClass eClass,
	@description("The super type to be removed") EClass superType = eClass.eSuperTypes[0] ->

	// constraints
	assert eClass.eSuperTypes.contains(superType) : "The super type to be removed actually has to be a super type of the class"
	
	// metamodel adaptation
	eClass.eSuperTypes.remove(superType)
	
	// model migration
	for(instance in eClass.allInstances) {
		for(feature in superType.eAllStructuralFeatures) {
			deleteFeatureValue(instance, feature)
		}
	}
}
removeSuperType = removeSuperTypeImpl



//added 4/12/08 due to PCM case study
@description("In the metamodel, the super type of a class is replaced by its super types. In the model, the values of the features that the class inherits from that super type (excluding its super types) are deleted.")
@label("Generalize Super Type")
@deleting
generalizeSuperTypeImpl = {
	@description("The class of which the super type is replaced") EClass eClass,
	@description("The super type to be replaced by its super types") EClass superType = eClass.eSuperTypes[0] ->

	// constraints
	assert eClass.eSuperTypes.contains(superType) : "The super type to remove actually has to be a super type"
	
	// metamodel adaptation
	eClass.eSuperTypes.remove(superType)
	eClass.eSuperTypes.addAll(superType.eSuperTypes)
	
	// model migration
	for(instance in eClass.allInstances) {
		for(EStructuralFeature feature in superType.eStructuralFeatures) {
			deleteFeatureValue(instance, feature)
		}
	}
}
generalizeSuperType = generalizeSuperTypeImpl



//added 7/12/08 due to PCM case study
@description("In the metamodel, the type of a containment reference is specialized by a new sub class. In the model, the values of this reference are migrated to the new type.")
@label("Specialize Composition")
@deleting
specializeCompositionImpl = {
	@description("The containment reference to be specialized") EReference reference,
	@description("The package in which the sub class is created") EPackage ePackage,
	@description("The name of the sub class") String name ->

	def EClass superType = reference.eType
	def EClass eClass = reference.eContainingClass
	
	// constraints
	assert reference.containment : "The reference has to be a containment reference"
	
	// metamodel adaptation
	def newType = ePackage.newEClass(name, [superType], false)
	reference.eType = newType
	
	// model migration
	for(instance in eClass.allInstances) {
		if(instance.isSet(reference)) {
			def value = instance.get(reference)
			if(reference.many) {
				for(v in value) {
					if(v.getEClass() == superType) {
						v.migrate(newType)
					}
					else {
						v.delete()
					}
				}
			}
			else if(value != null) {
				if(value.getEClass() == superType) {
					value.migrate(newType)
				}
				else {
					value.delete()
				}
			}
		}
	}
}
specializeComposition = specializeCompositionImpl



//added 31/12/2008 due to gmfgraph case study
@label("Generalize Attribute")
@description("In the metamodel, the multiplicity of an attribute is generalized. In the model, nothing is changed.")
generalizeAttributeImpl = {
	@description("The attribute to be generalized") EAttribute attribute,
	@description("The new lower bound of the attribute") int lowerBound = attribute.lowerBound,
	@description("The new upper bound of the attribute") int upperBound = attribute.upperBound ->
	
	// constraints
	assert lowerBound <= attribute.lowerBound && (upperBound >= attribute.upperBound || upperBound == -1) : "The multiplicity must be the same or more general"
	
	// metamodel adaptation
	attribute.lowerBound = lowerBound
	attribute.upperBound = upperBound
}
generalizeAttribute = generalizeAttributeImpl



//added 31/12/2008 due to gmfgraph case study
@label("Specialize Reference Type")
@description("In the metamodel, the type of a reference can be specialized to its subclass, in case it is abstract and has only one subclass. In the model, nothing is changed.")
specializeReferenceTypeImpl = {
	@description("The reference whose type is specialized") EReference reference,
	@description("The new type of the reference") EClass type ->
	
	def EClass oldType = reference.eType

	// constraints
	assert !isConcrete(oldType) : "The old type of the reference must be abstract"
	assert type.eSuperTypes.contains(oldType) : "The new type of the reference must be a subclass of its old type"
	assert oldType.eSubTypes.size() == 1 : "The old type must not have any other subclass"

	// metamodel adaptation
	reference.eType = type
}
specializeReferenceType = specializeReferenceTypeImpl



filterValueByType = { instance, reference, type ->
	if(reference.many) {
		def values = new ArrayList(instance.get(reference))
		for(value in values) {
			if(!value.instanceOf(type)) {
				instance.remove(reference, value)
				if(reference.containment) {
					value.delete()
				}
			}
		}
	} else {
		def value = instance.get(reference)
		if(value != null) {
			if(!value.instanceOf(type)) {
				instance.unset(reference)
				if(reference.containment) {
					value.delete()
				}
			}
		}
	}
}


filterValueByMultiplicity = { instance, reference, upperBound ->
	if(reference.many) {
		def values = new ArrayList(instance.get(reference))
		if(upperBound == 1 && values.size() > 1) {
			int i = 0;
			for(value in values) {
				if(i >= upperBound) {
					instance.remove(reference, value)
					if(reference.containment) {
						value.delete()
					}
				}
				i++;
			}
		}
	}
}


@description("In the metamodel, either the type or the multiplicity of a reference is specialized. In the model, values no longer conforming to the new type or multiplicity are removed.")
@label("Specialize Reference")
specializeReferenceImpl = {
	@description("The reference to be generalized") EReference reference,
	@description("The new type of the reference") EClass type = reference.eType,
	@description("The new lower bound of the reference") int lowerBound = reference.lowerBound,
	@description("The new upper bound of the reference") int upperBound = reference.upperBound ->

	// constraints
	assert type.eAllSuperTypes.plus(type).contains(reference.eType) : "The type must be the same or more special"
	assert lowerBound >= reference.lowerBound && (upperBound <= reference.upperBound || reference.upperBound == -1) : "The multiplicity must be the same or more special"

	// metamodel adaptation
	if(reference.eType != type) {
		reference.eType = type
		if(reference.eOpposite != null) {
			type.eStructuralFeatures.add(reference.eOpposite)
		}
	}
	
	// model migration
	for(instance in reference.eContainingClass.allInstances) {
		filterValueByType(instance, reference, type)
		filterValueByMultiplicity(instance, reference, type)
	}

	// metamodel adaptation
	reference.lowerBound = lowerBound
	reference.upperBound = upperBound
}
specializeReference = specializeReferenceImpl