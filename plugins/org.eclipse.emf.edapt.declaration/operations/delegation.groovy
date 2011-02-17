import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edaptcope.migration.*
import org.eclipse.emf.edaptcope.common.*


@description("In the metamodel, a number of features are extracted to a new class. This new class is accessible from the context class through a new containment reference. In the model, the values of the features are extracted to a new instance accordingly.")
@label("Extract Class")
extractClassImpl = {
	@description("The context class from which the features are extracted") EClass contextClass,
	@description("The features to be extracted") List<EStructuralFeature> features,
	@description("The package in which the extracted class is created") EPackage ePackage = contextClass.ePackage,
	@description("The name of the extracted class") String className,
	@description("The name of the new containment reference from context to extracted class") String referenceName ->

	// constraints
	assert contextClass.eStructuralFeatures.containsAll(features) : "The features have to belong to the same class"
	assert ePackage.getEClassifier(className) == null : "A classifier with the same name already exists"
	assert contextClass.getEStructuralFeature(referenceName) == null || features.contains(contextClass.getEStructuralFeature(referenceName)) : "A feature with the same name already exists"
	
	// metamodel adaptation
	def extractedClass = ePackage.newEClass(className)
	def reference = contextClass.newEReference(referenceName, extractedClass, 1, 1, true)
	extractedClass.eStructuralFeatures.addAll(features)
	for(feature in features) {
		if(feature instanceof EReference && feature.eOpposite != null) {
			feature.eOpposite.eType = extractedClass
		}
	}
	
	// model migration
	for(contextInstance in contextClass.allInstances) {
		def extractedInstance = extractedClass.newInstance()
		contextInstance.set(reference, extractedInstance);
		for(feature in features) {
			extractedInstance.set(feature, contextInstance.unset(feature))
		}
	}
}
extractClass = extractClassImpl



@description("In the metamodel, a class reachable through a single-valued containment reference is inlined. More specifically, its features are moved to the source class of the reference. In the model, the values of these features are moved accordingly.")
@label("Inline Class")
inlineClassImpl = {
	@description("The reference to the class to be inlined") EReference reference ->

	def EClass inlinedClass = reference.eReferenceType
	def EClass contextClass = reference.eContainingClass
	def List<EStructuralFeature> features = new ArrayList(inlinedClass.eStructuralFeatures)
	
	// contraints
	assert reference.eOpposite == null : "The reference must not have an opposite"
	assert reference.upperBound == 1 : "The multiplicity of the reference must be single-valued"
	assert reference.containment : "The reference must be containment"
	assert inlinedClass.eSubTypes.isEmpty() : "The class to be inlined must not have sub classes"
	assert inlinedClass.getInverse(emf.ETypedElement.eType).every{feature -> feature instanceof EAttribute || feature.eOpposite == null || features.contains(feature.eOpposite) } : "The class to be inlined must not be a type of another reference"
	
	// metamodel adaptation
	contextClass.eStructuralFeatures.addAll(features)
	for(feature in features) {
		if(feature instanceof EReference && feature.eOpposite != null) {
			feature.eOpposite.eType = contextClass
		}
	}
	reference.delete()
	inlinedClass.delete()
	
	// model migration
	for(contextElement in contextClass.allInstances) {
		inlinedElement = contextElement.unset(reference)
		if(inlinedElement != null) {
			for(feature in features) {
				contextElement.set(feature, inlinedElement.unset(feature))
			}
			inlinedElement.delete()
		}
	}
}
inlineClass = inlineClassImpl



@description("In the metamodel, the composite design pattern is introduced. More specifically, a class is refined by two sub classes - one for composite and one for leaf elements, and a reference is moved to the composite class. In addition, the class is made abstract. In the model, instances of that class are migrated based on whether the reference is populated or not.")
@label("Introduce Composite Pattern")
partitionCompositeImpl = {
	@description("The class which is refined") EClass eClass,
	@description("The name of the composite class") String compositeName,
	@description("The name of the leaf class") String leafName,
	@description("The reference for composite elements") EReference childReference ->
	
	// constraints
	assert eClass.eReferences.findAll{r -> r.containment}.contains(childReference) : "The child reference must be a containment reference defined by the class"
	assert eClass.eSubTypes.isEmpty() : "The class must not have sub classes"

	// metamodel adaptation
	eClass.'abstract' = true
	def ePackage = eClass.ePackage
	def compositeClass = ePackage.newEClass(compositeName, [eClass])
	compositeClass.eStructuralFeatures.add(childReference)
	def leafClass = ePackage.newEClass(leafName, [eClass])
	
	// model migration
	for(instance in eClass.instances) {
		if(instance.get(childReference).isEmpty()) {
			instance.migrate(leafClass)
		}
		else {
			instance.migrate(compositeClass)
		}
	}
}
partitionComposite = partitionCompositeImpl



@description("In the metamodel, a feature is moved along a single-valued reference. In the model, values are moved accordingly.")
@label("Move Feature along Reference")
moveFeatureImpl = {
	@description("The feature to be moved") EStructuralFeature feature,
	@description("The reference along which the feature is moved") EReference reference ->

	// variables
	def EClass sourceClass = feature.eContainingClass
	def EClass targetClass = reference.eReferenceType

	// constraints
	assert reference.lowerBound == 1 && reference.upperBound == 1 : "The multiplicity of the reference must be single-valued and obligatory"
	assert reference.eOpposite == null || reference.eOpposite.upperBound == 1 : "The multiplicity of its opposite reference must be single-valued"
	assert sourceClass.eAllStructuralFeatures.contains(reference) : "The reference must be available in the same class as the feature"
	assert targetClass.getEStructuralFeature(feature.name) == null : "A feature with that name already exists in the target class"
	
	// metamodel adaptation
	targetClass.eStructuralFeatures.add(feature)
	
	// model migration
	for(instance in sourceClass.allInstances) {
		def target = instance.get(reference)
		if(instance.isSet(feature)) {
			def value = instance.unset(feature)
			target.set(feature, value)
		}
	}
}
moveFeature = moveFeatureImpl



//added due to GMF case study
@description("In the metamodel, a feature is moved opposite to a multi-valued reference. In the model, the values of the feature are aggregated accordingly.")
@label("Collect Feature over Reference")
@deleting
collectFeatureImpl = {
	@description("The feature to be moved") EStructuralFeature feature,
	@description("The reference opposite to which the feature is moved") EReference reference ->
	
	// variables
	def EClass sourceClass = feature.eContainingClass
	def EClass targetClass = reference.eContainingClass
	
	// constraints
	assert (feature.many && reference.many) || !reference.many : "Both feature and reference must be multi-valued or the reference must be single-valued"
	assert reference.eReferenceType.eStructuralFeatures.contains(feature) : "The feature must belong to the reference's type"
	
	// metamodel adaptation
	targetClass.eStructuralFeatures.add(feature)
	
	// model migration
	for(target in targetClass.allInstances) {
		if(reference.many) {
			for(source in target.get(reference)) {
				def sourceValue = source.unset(feature)
				target.get(feature).addAll(sourceValue)
			}
		}
		else {
			def source = target.get(reference)
			if(source != null) {
				target.set(feature, source.unset(feature))
			}
		}
	}
	for(source in sourceClass.allInstances) {
		deleteFeatureValue(source, feature)
	}
}
collectFeature = collectFeatureImpl



//added due to GMF case study
@description("In the metamodel, a number of features are combined in to a single feature by moving it over references to the same class. In the model, the values of the features are moved accordingly.")
@label("Combine Features over References")
combineFeatureImpl = {
	@description("The features to be combined") List<EStructuralFeature> features,
	@description("The references over which the features are moved (in the same order)") List<EReference> references ->

	def EClass eClass = references[0].eType
	def EStructuralFeature mainFeature = features[0]
	
	// constraints
	assert references.every{it -> it.eType == eClass} : "All references must have the same class as type"
	assert features.size() == references.size() : "There must be an equal number of features and references"
	assert references.every{it -> it.eContainingClass == features[references.indexOf(it)].eContainingClass} : "Each feature has to belong to its reference's class"
	
	// metamodel adaptation
	eClass.eStructuralFeatures.add(mainFeature)
	for(feature in features) {
		if(feature != mainFeature) {
			feature.delete()
		}
	}
	
	// model migration
	for(int i = 0; i < references.size(); i++) {
		def reference = references[i]
		def feature = features[i]
		for(instance in reference.eContainingClass.allInstances) {
			def value = instance.unset(feature)
			def ref = instance.get(reference)
			if(ref != null) {
				ref.set(mainFeature, value)
			}
		}
	}
}
combineFeature = combineFeatureImpl



//added due to GMF case study
@description("In the metamodel, a feature is propagated opposite to a number of references. More specifically, the feature is created in each of the classes which are sources of the references. In the model, the values of that feature are moved accordingly.")
@label("Propagate Feature over References")
propagateFeatureImpl = {
	@description("The feature to be propagated") EStructuralFeature mainFeature,
	@description("The references opposite to which the feature is propagated") List<EReference> references ->

	// variables
	def EClass eClass = mainFeature.eContainingClass
	
	// constraints
	assert references.every{it -> it.eType == eClass} : "Every reference has to target the class with the feature"
	
	// metamodel adaptation
	boolean first = true
	def features = []
	for(reference in references) {
		def feature = null
		if(first) {
			feature = mainFeature
			first = false
		}
		else {
			feature = mainFeature.clone()
		}
		features.add(feature)
		reference.eContainingClass.eStructuralFeatures.add(feature)
	}
	
	// model migration
	for(int i = 0; i < references.size(); i++) {
		def reference = references[i]
		def feature = features[i]
		for(instance in reference.eContainingClass.allInstances) {
			def ref = instance.get(reference)
			if(ref != null) {
				def value = ref.unset(mainFeature)
				instance.set(feature, value)
			}
		}
	}
}
propagateFeature = propagateFeatureImpl



//added 21/12/2008 due to PCM case study
@description("In the metamodel, a number of features are extracted into an existing class. More specifically, a containment reference to the extracted class is created and the features are replaced by features of the extracted class. In the model, the values of the features are moved accordingly to a new instance of the extracted class.")
@label("Fold Class")
extractExistingClassImpl = {
	@description("The features to be extracted") List<EStructuralFeature> toReplace,
	@description("The extracted class") EClass extractedClass,
	@description("The features of the extracted class by which they are replaced (in the same order)") List<EStructuralFeature> replaceBy,
	@description("The name of the containment reference") String referenceName ->

	def EClass contextClass = toReplace[0].eContainingClass
	
	assert toReplace.size() == replaceBy.size() : "The replaced and replacing features must be of the same size"
	assert extractedClass.eAllStructuralFeatures.containsAll(replaceBy) : "The features to replace must be defined in the extracted class"
	assert toReplace.every{it.eType == replaceBy[toReplace.indexOf(it)].eType} : "The features must be of the same type"
	assert toReplace.every{it.many == replaceBy[toReplace.indexOf(it)].many} : "The features must be of the same multiplicity"
	
	def reference = contextClass.newEReference(referenceName, extractedClass, 1, 1, true)
	for(feature in toReplace) {
		feature.delete()
	}
	
	for(contextInstance in contextClass.allInstances) {
		def extractedInstance = extractedClass.newInstance()
		contextInstance.set(reference, extractedInstance)
		for(int i = 0; i < toReplace.size(); i++) {
			extractedInstance.set(replaceBy[i], contextInstance.unset(toReplace[i]))
		}
	}
}
extractExistingClass = extractExistingClassImpl



//added 19/02/2009 due to GMF case study
@label("Extract and Group Attribute")
@description("In the metamodel, an attribute is extracted into a new class. This extracted class is contained by an existing container class and referenced from the context class. In the model, an instance of the extracted class is created for each different value of the extracted attribute.")
extractAndGroupAttributeImpl = {
	@description("The attribute to be extracted") EAttribute extractedAttribute,
	@description("The package in which the extracted class is created") EPackage contextPackage,
	@description("The name of the extracted class") String extractedClassName,
	@description("The reference from the context class to the extracted class") String referenceName,
	@description("The container class for the extracted class") EClass containerClass,
	@description("The name of the containment reference from the container class to the extracted class") String containerReferenceName ->

	def EClass contextClass = extractedAttribute.eContainingClass
	
	// constraints
	assert !extractedAttribute.many : "The extracted attribute must be single-valued"

	// metamodel adaptation
	def extractedClass = contextPackage.newEClass(extractedClassName)
	extractedClass.eStructuralFeatures.add(extractedAttribute)
	extractedAttribute.lowerBound = 1
	
	def reference = contextClass.newEReference(referenceName, extractedClass, 0, 1, false)
	
	def containerReference = containerClass.newEReference(containerReferenceName, extractedClass, 0, -1, true)
	
	// model migration
	for(contextElement in contextClass.allInstances) {
		def value = contextElement.unset(extractedAttribute)
		if(value != null) {
			def containerElement = contextElement;
			while(containerElement != null && !(containerElement.instanceOf(containerClass))) {
				containerElement = containerElement.getContainer()
			}
			if(containerElement != null) {
				def extractedElement = containerElement.get(containerReference).find{e -> value.equals(e.get(extractedAttribute))}
				if(extractedElement == null) {
					extractedElement = extractedClass.newInstance()
					extractedElement.set(extractedAttribute, value)
					containerElement.get(containerReference).add(extractedElement)
				}
				contextElement.set(reference, extractedElement)
			}
		}
	}
}
extractAndGroupAttribute = extractAndGroupAttributeImpl



//added 19/02/2009 due to GMF case study
@label("Flatten Containment Hierarchy")
@description("In the metamodel, a containment hierarchy is flattened. More specifically, the reference to denote the root as well as the reference to denote the children are replaced by a containment reference. In the model, the corresponding hierarchies are flattened accordingly.")
@deleting
flattenHierarchyImpl = {
	@description("The reference to denote the root node") EReference rootReference,
	@description("The reference to denote the children nodes") EReference childrenReference,
	@description("The reference which replaces the containment hierarchy") String referenceName ->

	def EClass rootClass = rootReference.eContainingClass
	def EClass nodeClass = rootReference.eType
	
	// constraints
	assert !rootReference.many && rootReference.containment : "The root reference must be a single-valued containment reference."
	assert childrenReference.many && childrenReference.containment : "The children reference must be a multi-valued containment reference."
	assert nodeClass.eStructuralFeatures.contains(childrenReference) : "The children reference must be defined by the node class."
	assert childrenReference.eType == nodeClass : "The type of the children reference must be the node class."
	
	// metamodel adaptation
	rootReference.delete()
	childrenReference.delete()
	def containerReference = rootClass.newEReference(referenceName, nodeClass, 0, -1, true)
	
	// model migration
	visitNode = { root, node ->
		def children = node.unset(childrenReference)
		root.get(containerReference).addAll(children)
		for(child in children) {
			visitNode(root, child)
		}
	}
	
	for(root in rootClass.allInstances) {
		def node = root.unset(rootReference)
		if(node != null) {
			root.get(containerReference).add(node)
			visitNode(root, node)
		}
	}
}
flattenHierarchy = flattenHierarchyImpl


// Added due to the UML Activity case study
@description("In the metamodel, a class reachable through a single-valued containment reference is unfolded. More specifically, its features are copied to the source class of the reference which is deleted. In the model, the values of these features are moved accordingly.")
@label("Unfold Class")
unfoldClassImpl = {
	@description("The reference to the class to be unfolded") EReference reference ->

	def EClass unfoldedClass = reference.eReferenceType
	def EClass contextClass = reference.eContainingClass
	def List<EStructuralFeature> features = new ArrayList(unfoldedClass.eAllStructuralFeatures)
	
	// contraints
	assert reference.eOpposite == null : "The reference must not have an opposite"
	assert reference.upperBound == 1 : "The multiplicity of the reference must be single-valued"
	assert reference.containment : "The reference must be containment"
	assert unfoldedClass.eSubTypes.isEmpty() : "The class to be unfolded must not have sub classes"
	
	// metamodel adaptation
	def unfoldedFeatures = []
	for(feature in features) {
		def unfoldedFeature = feature.clone()
		unfoldedFeatures.add(unfoldedFeature)
		if(contextClass.getEStructuralFeature(feature.name) != null) {
			unfoldedFeature.name = unfoldedFeature.name + "_" + unfoldedClass.name
		}
		contextClass.eStructuralFeatures.add(unfoldedFeature)
		if(feature instanceof EReference && feature.eOpposite != null) {
			def foldedOpposite = feature.eOpposite.clone()
			foldedOpposite.eType = contextClass
			foldedOpposite.name = foldedOpposite.name + "_" + contextClass.name
			feature.eType.eStructuralFeatures.add(foldedOpposite)
			foldedOpposite.eOpposite = unfoldedFeature 
		}
	}
	reference.delete()
	
	// model migration
	for(contextElement in contextClass.allInstances) {
		unfoldedElement = contextElement.unset(reference)
		if(unfoldedElement != null) {
			int i = 0;
			for(feature in features) {
				contextElement.set(unfoldedFeatures[i], unfoldedElement.unset(feature))
				i++;
			}
			unfoldedElement.delete()
		}
	}
}
unfoldClass = unfoldClassImpl

