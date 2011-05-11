import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edapt.migration.*
import org.eclipse.emf.edapt.common.*



@label("Replace Feature")
@description("In the metamodel, a feature is replace by another one. In the model, the values are moved accordingly.")
@deprecated
replaceFeatureImpl = {
	@description("The feature to be replaced") EStructuralFeature toReplace,
	@description("The feature by which it is replaced") EStructuralFeature replaceBy ->

	def EClass subClass = toReplace.eContainingClass
	
	assert toReplace.eType == replaceBy.eType : "The features must be of the same type"
	assert toReplace.many == replaceBy.many : "The features must be of the same cardinality"
	assert subClass.eAllStructuralFeatures.contains(replaceBy) : "The feature to replace must be defined in a sub class of the one with the feature by which it is replaced"

	// metamodel adaptation
	toReplace.delete()
	
	// model migration
	for(instance in subClass.allInstances) {
		instance.set(replaceBy, instance.unset(toReplace))
	}
}
replaceFeature = replaceFeatureImpl



@description("In the metamodel, a super class is extracted from a number of sub classes. In the model, nothing is changed.")
@label("Extract Super Class")
extractSuperClass2Impl = {
	@description("The class from which the super class is extracted") List<EClass> subClasses,
	@description("The features to be extracted") List<EStructuralFeature> toExtract,
	@description("The package in which the super class is created") EPackage ePackage = subClass.ePackage,
	@description("The name of the super class") String superClassName,
	@description("Whether the super class is abstract") boolean abstr = true,
	@description("The super classes of the sub class which become super classes of the super class") List<EClass> superSuperClasses = [] ->

	def EStructuralFeature mainFeature = toExtract.isEmpty() ? null : toExtract[0]

	// constraints
	assert mainFeature == null || toExtract.every{feature -> mainFeature.eType == feature.eType} : "The features' types have to be the same"
	assert mainFeature == null || toExtract.every{feature -> mainFeature.lowerBound == feature.lowerBound && mainFeature.upperBound == feature.upperBound} : "The features' multiplicities have to be the same"
	assert mainFeature == null || mainFeature instanceof EAttribute || toExtract.every{feature -> mainFeature.containment == feature.containment} : "The features have to be all containment references or not"
	assert mainFeature == null || mainFeature instanceof EAttribute || toExtract.every{feature -> (feature == mainFeature || feature.eOpposite == null)} : "The features must not have opposite references"
	assert subClasses.every{subClass -> subClass.eSuperTypes.containsAll(superSuperClasses)} : "The sub classes must have the super classes as common super classes"

	// metamodel adaptation
	def superClass = ePackage.newEClass(superClassName, superSuperClasses, abstr)
	for(subClass in subClasses) {
		subClass.eSuperTypes.add(superClass)
		subClass.eSuperTypes.removeAll(superSuperClasses)
	}
	
	if(!toExtract.isEmpty()) {
		pullFeature(toExtract, superClass)
	}
}
extractSuperClass2 = extractSuperClass2Impl



@description("In the metamodel, a number of features of a class are extracted to a new super class. In the model, nothing is changed.")
@label("Extract Super Class")
@deprecated
extractSuperClassImpl = {
	@description("The class from which the features are extracted") EClass subClass,
	@description("The features to be extracted") List<EStructuralFeature> toExtract,
	@description("The package in which the super class is created") EPackage ePackage = subClass.ePackage,
	@description("The name of the super class") String superClassName,
	@description("Whether the super class is abstract") boolean abstr = true,
	@description("The super classes of the sub class which become super classes of the super class") List<EClass> superSuperClasses = [] ->

	// constraints
	assert subClass.eStructuralFeatures.containsAll(toExtract) : "The features to be extracted must belong to sub class"
	assert subClass.eSuperTypes.containsAll(superSuperClasses) : "The super classes to be extracted must be a containsAll of the subclass's super types"

	// metamodel adaptation
	def superClass = ePackage.newEClass(superClassName)
	superClass.'abstract' = abstr
	superClass.eStructuralFeatures.addAll(toExtract)
	superClass.eSuperTypes.addAll(superSuperClasses)
	
	subClass.eSuperTypes.add(superClass)
	subClass.eSuperTypes.removeAll(superSuperClasses)

	for(EStructuralFeature feature : toExtract) {
		if(feature instanceof EReference) {
			EReference reference = (EReference) feature
			if(reference.eOpposite != null) {
				reference.eOpposite.eType = superClass
			}
		}
	}
}
extractSuperClass = extractSuperClassImpl



@description("In the metamodel, a number of features are replaced by features of a new super class. In the model, the values are moved to these features based on a mapping.")
@label("Fold Super Class")
useSuperClassImpl = {
	@description("The class to which the super class is added") EClass subClass,
	@description("The new super class") EClass superClass,
	@description("The features to be replaced") List<EStructuralFeature> toReplace,
	@description("The features by which they are replaced (in the same order)") List<EStructuralFeature> replaceBy ->

	// constraints
	assert toReplace.size() == replaceBy.size() : "The number of features to be replaced and to replace them must be the same"
	assert subClass.eStructuralFeatures.containsAll(toReplace) : "The features to be replaced must belong to the sub class"
	assert superClass.eAllStructuralFeatures.containsAll(replaceBy) : "The features to replace must be available in the super class"
	assert toReplace.every{it.eType == replaceBy[toReplace.indexOf(it)].eType} : "The features must be of the same type"
	assert toReplace.every{it.many == replaceBy[toReplace.indexOf(it)].many} : "The features must be of the same multiplicity"

	subClass.eSuperTypes.add(superClass)
	subClass.eSuperTypes.removeAll(superClass.eAllSuperTypes)
	
	for(int i = 0; i < toReplace.size(); i++) {
		replaceFeature(toReplace[i], replaceBy[i])
	}
}
useSuperClass = useSuperClassImpl



@description("In the metamodel, a super class is inlined into its sub classes. More specifically, its features are propagated to the sub classes. In the model, the values of these features have to be adapted accordingly.")
@label("Inline Super Class")
inlineSuperClassImpl = {
	@description("The super class to be inlined") EClass superClass ->
		
	// constraints
	assert !isConcrete(superClass) : "The super class must be abstract"
	
	for(EStructuralFeature feature : new ArrayList(superClass.eStructuralFeatures)) {
		pushFeature(feature)
	}
	
	List<EClass> subClasses = superClass.getInverse(emf.EClass._eSuperTypes)
	for(EClass subClass in subClasses) {
		subClass.eSuperTypes.remove(superClass)
		for(EClass superSuperClass in superClass.eSuperTypes) {
			if(!subClass.eAllSuperTypes.contains(superSuperClass)) {
				subClass.eSuperTypes.add(superSuperClass)
			}
		}
	}
	
	superClass.delete()
}
inlineSuperClass = inlineSuperClassImpl



@description("In the metamodel, a number of features are pulled up into a common super class. In the model, values are changed accordingly.")
@label("Pull up Feature")
pullFeatureImpl = {
	@description("The features to be pulled up") List<EStructuralFeature> features,
	@description("The super class to which the features are pulled") EClass targetClass = features[0].eContainingClass.eSuperTypes[0] ->

	def EStructuralFeature mainFeature = features[0]

	// constraints
	assert features.every{feature -> mainFeature.eType == feature.eType} : "The features' types have to be the same"
	assert features.every{feature -> mainFeature.lowerBound == feature.lowerBound && mainFeature.upperBound == feature.upperBound} : "The features' multiplicities have to be the same"
	assert mainFeature instanceof EAttribute || features.every{feature -> mainFeature.containment == feature.containment} : "The features have to be all containment references or not"
	assert mainFeature instanceof EAttribute || features.every{feature -> (feature == mainFeature || feature.eOpposite == null)} : "The features must not have opposite references"
	assert features.every{feature -> feature.eContainingClass.eSuperTypes.contains(targetClass)} : "The features' classes must have a common super type"
	
	targetClass.eStructuralFeatures.add(mainFeature)
	if(mainFeature instanceof EReference && mainFeature.eOpposite != null) {
		generalizeReference(mainFeature.eOpposite, targetClass)
	}
	for(feature in features) {
		if(feature != mainFeature) {
			replaceFeature(feature, mainFeature)
		}
	}	
}
pullFeature = pullFeatureImpl



@description("In the metamodel, a feature is pushed down to its sub classes. In the model, values are changed accordingly.")
@label("Push down Feature")
@deleting
pushFeatureImpl = {
	@description("The feature to be pushed down") EStructuralFeature feature ->
	
	EClass superClass = feature.eContainingClass
	List<EClass> subClasses = superClass.getInverse(emf.EClass._eSuperTypes)
	
	// constraints
	assert feature instanceof EAttribute || feature.eOpposite == null || superClass.eSubTypes.size() == 1 : "If the feature has an opposite, then the super class may only have one sub type."
	
	def first = true
	for(subClass in subClasses) {
		if(first) {
			subClass.eStructuralFeatures.add(feature)
			if(feature instanceof EReference && feature.eOpposite != null) {
				feature.eOpposite.eType = subClass
			}
		}
		else {
			// metamodel adaptation
			def clone = feature.clone()
			subClass.eStructuralFeatures.add(clone)
	
			// model migration
			for(instance in subClass.allInstances) {
				instance.set(clone, instance.unset(feature))
			}
		}
		first = false;
	}
	
	if(isConcrete(superClass)) {
		for(instance in superClass.instances) {
			deleteFeatureValue(instance, feature)
		}
	}
}
pushFeature = pushFeatureImpl



//added 16/12/2008 due to unicase case study
@description("In the metamodel, the sub class is deleted. In the model, all instances of this sub class are migrated to its super class.")
@label("Inline Sub Class")
inlineSubClassImpl = {
	@description("The class to be inlined") EClass subClass ->
	
	def EClass superClass = subClass.eSuperTypes[0]

	assert subClass.eSuperTypes.size() == 1 : "The sub class must have exactly one super type"
	assert subClass.eSubTypes.isEmpty() : "The sub class must not have sub types"
	assert subClass.eStructuralFeatures.isEmpty() : "The sub class must not have features"
	assert isConcrete(superClass) : "The super class must not be abstract"
	
	// metamodel adaptation
	subClass.delete()
	
	// model migration
	for(instance in subClass.allInstances) {
		instance.migrate(superClass)
	}
}
inlineSubClass = inlineSubClassImpl



//added 4/1/2009 due to gmfmap case study
@label("Extract Subclass")
@description("In the metamodel, a feature is extracted into a new subclass and the feature is made mandatory. In the model, all instances of the superclass that have the feature set are migrated to the new subclass.")
extractSubClassImpl = {
	@description("The superclass from which the feature is extracted") EClass superClass,
	@description("The feature to be extracted") EStructuralFeature feature,
	@description("The name of the new subclass") String className ->

	// constraints
	assert superClass.eStructuralFeatures.contains(feature) : "The feature has to belong to the super class"
	assert superClass.eSubTypes.isEmpty() : "The super class may not have a sub class"
	
	// metamodel adaptation
	def ePackage = superClass.ePackage
	def subClass = ePackage.newEClass(className, [superClass])
	subClass.eStructuralFeatures.add(feature)
	feature.lowerBound = 1
	
	// model migration
	for(instance in superClass.instances) {
		if(instance.isSet(feature)) {
			instance.migrate(subClass)
		}
	}
}
extractSubClass = extractSubClassImpl



//added 31/12/2008 due to gmfgraph case study
@label("Unfold Superclass")
@description("In the metamodel, a superclass is removed from a subclass, while all its features are copied into the subclass. In the model, values are changed accordingly.")
imitateSuperTypeImpl = {
	@description("The subclass") EClass subClass,
	@description("The superclass") EClass superClass = subClass.eSuperTypes[0] ->

	def List<EStructuralFeature> features = superClass.eStructuralFeatures
	
	// constraints
	assert subClass.eSuperTypes.contains(superClass) : "The super class has to be a super type of the sub class"
	assert superClass.getInverse(emf.ETypedElement.eType).isEmpty() : "The super class must not be target of a reference"
	
	// metamodel adaptation
	subClass.eSuperTypes.remove(superClass)
	subClass.eSuperTypes.addAll(superClass.eSuperTypes)
	
	def clones = []
	for(feature in features) {
		def clone = feature.clone()
		subClass.eStructuralFeatures.add(clone)
		clones.add(clone)
	}
	
	// model migration
	for(instance in subClass.allInstances) {
		for(int i = 0; i < features.size(); i++) {
			instance.set(clones[i], instance.unset(features[i]))
		}
	}
}
imitateSuperType = imitateSuperTypeImpl



//added 1/5/2009 due to the GMF case study
@label("Pull up Operation")
@description("In the metamodel, a number of operations are pulled up into a common super class. In the model, nothing needs to be done.")
pullOperationImpl = {
	@description("The operations to be pulled up") List<EOperation> operations,
	@description("The super class to which the operations are pulled") EClass targetClass = operations[0].eContainingClass.eSuperTypes[0] ->

	// variables
	def EOperation mainOperation = operations[0]

	// constraints
	assert operations.every{operation -> mainOperation.eType == operation.eType} : "The operations' types have to be the same"
	assert operations.every{operation -> mainOperation.lowerBound == operation.lowerBound && mainOperation.upperBound == operation.upperBound} : "The operations' multiplicities have to be the same"
	assert operations.every{operation -> operation.eContainingClass.eSuperTypes.contains(targetClass)} : "The operations' classes must have a common super type"
	
	// metamodel adaptation
	targetClass.eOperations.add(mainOperation)
	for(operation in operations) {
		if(operation != mainOperation) {
			operation.delete()
		}
	}	
}
pullOperation = pullOperationImpl



//added 1/5/2009 due to the GMF case study
@label("Push down Operation")
@description("In the metamodel, an operation is pushed down to its sub classes. In the model, nothing needs to be done.")
pushOperationImpl = {
	@description("The operation to be pushed down") EOperation operation ->
	
	// variables
	EClass superClass = operation.eContainingClass
	List<EClass> subClasses = superClass.eSubTypes
	
	// metamodel adaptation
	def first = true
	for(subClass in subClasses) {
		if(first) {
			subClass.eOperations.add(operation)
		}
		else {
			def clone = operation.clone()
			subClass.eOperations.add(clone)
		}
		first = false;
	}
}
pushOperation = pushOperationImpl


