import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edapt.migration.*
import org.eclipse.emf.edapt.common.*



// added 7/12/08 due to PCM case study
@description("In the metamodel, a class is deleted. In the model, its instances are migrated to a class sharing the same super class.")
@label("Replace Class by Sibling")
@deprecated
replaceClassBySiblingImpl = {
	@description("The class to be replaced") EClass toReplace,
	@description("The class by which it is replaced") EClass replaceBy ->
	
	// constraints
	assert toReplace.eSuperTypes.size() == 1 : "The replaced class must have exactly one super class"
	assert replaceBy.eSuperTypes.size() == 1 : "The replacing class must have exactly one super class"
	assert toReplace.eSuperTypes[0] == replaceBy.eSuperTypes[0] : "The super classes of replaced and replacing class must be the same"
	assert toReplace.eSubTypes.isEmpty() : "The replaced class must not have sub classes"
	assert toReplace.eStructuralFeatures.isEmpty() : "The replaced class must not have any features"
	
	// metamodel adaptation
	for(reference in toReplace.getInverse(emf.EReference.eType)) {
		reference.eType = replaceBy
	}
	toReplace.delete()
	
	// model migration
	for(instance in toReplace.allInstances) {
		instance.migrate(replaceBy)
	}
}
replaceClassBySibling = replaceClassBySiblingImpl



//added 22/12/2008 due to GMF case study
@description("In the metamodel, a class is deleted. In the model, instances of this class are migrated to another class based on a mapping of features.")
@label("Replace Class")
replaceClassImpl = {
	@description("The class to be replaced") EClass toReplace,
	@description("The class by which it is replaced") EClass replaceBy,
	@description("The features to be replaced") List<EStructuralFeature> featuresToReplace = [],
	@description("The features by which they are replaced (in the same order)") List<EStructuralFeature> featuresReplaceBy = [] ->

	// constraints
	assert toReplace.getInverse(emf.EClass._eSuperTypes).isEmpty() : "The class to be replaced must not have sub types"
	assert featuresToReplace.size() == featuresReplaceBy.size() : "The replaced and replacing features have to be of the same size"
	assert toReplace.eAllStructuralFeatures.containsAll(featuresToReplace) : "The replace features must be defined in the replaced class"
	assert replaceBy.eAllStructuralFeatures.containsAll(featuresReplaceBy) : "The replacing features must be defined in the replacing class"
	assert subtractFeatures(toReplace, replaceBy).every{feature -> featuresToReplace.contains(feature)} : "The replace features must cover all features from the difference between the class to replace and the class by which it is replaced"
	
	// metamodel adaptation
	for(reference in toReplace.getInverse(emf.EReference.eType)) {
		reference.eType = replaceBy
	}
	toReplace.delete()
	
	// model migration
	for(instance in toReplace.allInstances) {
		instance.migrate(replaceBy)		
		for(int i = 0; i < featuresToReplace.size(); i++) {
			instance.set(featuresReplaceBy[i], instance.unset(featuresToReplace[i]))
		}
	}
}
replaceClass = replaceClassImpl



@label("Split String Attribute")
@description("In the metamodel, a new String-typed attribute is created. In the model, the value of another String-typed attribute is split among the two attributes by means of a regular expression.")
splitStringAttributeImpl = {
	@description("The feature whose values are split") EAttribute toSplit,
	@description("The class in which the new feature is created") EClass context,
	@description("The name of the new attribute") String attributeName,
	@description("The regular expression") String pattern ->

	// variables
	def EClass eClass = toSplit.eContainingClass

	// constraints
	assert eClass.eAllSubTypes.plus(eClass).contains(context) : "The class with the new attribute must be a subclass of the class with the attribute to be split"
	assert toSplit.eType == emf.EString : "The type of the attribute to split has to be String"

	// metamodel adaptation
	def newAttribute = context.newEAttribute(attributeName, emf.EString);
	
	// model migration
	for(instance in context.allInstances) {
		value = instance.get(toSplit);
		fragments = value.split(pattern);
		if(fragments.length > 1) {
			instance.set(toSplit, fragments[0]);
			instance.set(newEAttribute, fragments[1]);
		}
	}
}
splitStringAttribute = splitStringAttributeImpl



@description("In the metamodel, a reference is deleted. In the model, the values of this reference are merged to a compatible reference.")
@label("Merge Reference into Another")
mergeImpl = {
	@description("The reference that is deleted") EReference toMerge,
	@description("The reference to which the values are merged") EReference mergeTo ->

	def EClass contextClass = toMerge.eContainingClass
	
	// constraints
	assert contextClass.eAllStructuralFeatures.contains(mergeTo) : "The reference to merge to must be available in the context class"
	assert mergeTo.many : "The reference to merge to must be multi-valued"
	assert toMerge.eReferenceType == mergeTo.eReferenceType || toMerge.eReferenceType.eAllSuperTypes.contains(mergeTo.eReferenceType) : "The types of the references must be compatible"
	assert toMerge != mergeTo : "The references must be different from each other"
	
	// model migration
	for(instance in contextClass.allInstances) {
		def value = instance.unset(toMerge)
		if(toMerge.many) {
			instance.get(mergeTo).addAll(value)
		}
		else {
			instance.get(mergeTo).add(value)
		}
	}
	
	// metamodel adaptation
	toMerge.delete()	
	if(toMerge.eOpposite != null) {
		toMerge.eOpposite.delete()
	}
}
merge = mergeImpl



//added 27/2/09 due to GMF case study
@label("Replace Literal")
@description("In the metamodel, an enum literal is removed and replaced by another one. In the model, the enum's values are replaced accordingly.")
replaceLiteralImpl = {
	@description("The enum literal to replace") EEnumLiteral toReplace,
	@description("The enum literal by which it is replaced") EEnumLiteral replaceBy ->

	def EEnum contextEnum = toReplace.eEnum

	// constraints
	assert contextEnum.eLiterals.contains(replaceBy) : "The enum literal by which it is replace must belong to the same enum."
	
	// metamodel adaptation
	toReplace.delete()
	
	// model migration
	def attributes = contextEnum.getInverse(emf.EAttribute.eType)
	for(attribute in attributes) {
		def contextClass = attribute.eContainingClass
		for(instance in contextClass.allInstances) {
			if(instance.get(attribute) == toReplace) {
				instance.set(attribute, replaceBy)
			}
		}
	}
}
replaceLiteral = replaceLiteralImpl



@description("In the metamodel, a number of references are united into a single reference which obtains their common super type as type. In the model, their values have to be moved accordingly.")
@label("Unite References")
uniteReferencesImpl = {
	@description("The references which are united") List<EReference> references,
	@description("The name of the single reference which unites all the references") String unitedReferenceName ->
	
	def EReference mainReference = references[0]
	def EClass contextClass = mainReference.eContainingClass
	
	assert contextClass.eStructuralFeatures.containsAll(references) : "The references have to belong to the same class"
	assert references.every{reference -> reference.containment == mainReference.containment} : "The references must be all either cross or containment references"

	// metamodel adaptation
	def referenceTypes = []
	for(reference in references) {
		referenceTypes.add(reference.eType)
		reference.delete()
	}
	def type = TypeUtils.leastCommonAncestor(referenceTypes)
	def unitedReference = contextClass.newEReference(unitedReferenceName, type, 0, -1, mainReference.containment)
	
	// model migration
	for(contextElement in contextClass.allInstances) {
		for(reference in references) {
			if(reference.many) {
				def values = contextElement.unset(reference)
				contextElement.get(unitedReference).addAll(values)
			} else {
				def value = contextElement.unset(reference)
				if(value != null) {
					contextElement.get(unitedReference).add(value)
				}
			}
		}
	}
}
uniteReferences = uniteReferencesImpl



@label("Partition Reference")
@description("In the metamodel, a reference is partitioned into a number of references according to its type. " +
		"A sub reference is created for each subclass of the reference's type. " +
		"Finally, the original reference is deleted. " +
		"In the model, the value of the reference is partitioned accordingly.")
partitionReferenceImpl = {
	@description("The reference to be partitioned") EReference reference ->
	
	EClass contextClass = reference.eContainingClass
	EClass type = reference.eReferenceType
	
	assert !isConcrete(type) : "The type of the reference must be abstract"
	assert reference.many : "The reference must be multi-valued"
	
	// metamodel adaptation
	def subReferences = []
	for(subClass in type.eSubTypes) {
		def name = subClass.name.substring(0,1).toLowerCase() + subClass.name.substring(1)
		def subReference = contextClass.newEReference(name, subClass, 0, -1, reference.containment)
		subReferences.add(subReference)
	}
	
	reference.delete()
	
	// model migration
	for(instance in contextClass.allInstances) {
		def values = instance.unset(reference)
		for(value in values) {
			def subReference = subReferences.find{r -> value.instanceOf(r.eReferenceType)}
			instance.get(subReference).add(value)
		}
	}
}
partitionReference = partitionReferenceImpl



//added due to GMF case study
@label("Replace Enumeration")
@description("In the metamodel, an enumeration is replaced by another one. More specifically, the enumeration is deleted and the other enumeration used instead. In the model, the values of this enumeration are replaced based on a mapping of literals.")
replaceEnumImpl = {
	@description("The enumeration to be replaced") EEnum toReplace,
	@description("The enumeration by which it is replaced") EEnum replaceBy,
	@description("The literals to be replaced") List<EEnumLiteral> literalsToReplace,
	@description("The literals by which they are replaced (in the same order)") List<EEnumLiteral> literalsReplaceBy ->

	// variables
	
	// constraints
	assert literalsToReplace.size() == literalsReplaceBy.size() : "The replacing and replaced literals must be of the same size"
	assert toReplace.eLiterals.containsAll(literalsToReplace) : "The replaced literals must belong to the replaced enumeration"
	assert replaceBy.eLiterals.containsAll(literalsReplaceBy) : "The replacing literals must belong to the replacing enumeration"
	
	// metamodel adaptation
	def attributes = toReplace.getInverse(emf.EAttribute.eType)
	for(attribute in attributes) {
		attribute.eType = replaceBy
	}
	toReplace.delete()
	
	// model migration
	for(attribute in attributes) {
		def eClass = attribute.eContainingClass
		for(instance in eClass.allInstances) {
			if(instance.isSet(attribute)) {
				def value = instance.get(attribute)
				int index = literalsToReplace.indexOf(value)
				instance.set(literalsReplaceBy[index])
			}
		}
	}
}
replaceEnum = replaceEnumImpl


