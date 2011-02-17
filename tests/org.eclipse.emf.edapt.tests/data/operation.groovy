@label("Extract Part Class")
extractPartClassImpl = {List<EStructuralFeature> features, EPackage ePackage = features[0].eContainingClass.ePackage, String partClassName, String partReferenceName ->

	// variables
	def EClass contextClass = features[0].eContainingClass

	// constraints
	assert features.every{feature -> feature.eContainingClass == contextClass} : "Features have to belong to the same class"
	assert ePackage.getEClassifier(partClassName) == null : "Classifier with the same name already exists"
	assert contextClass.getEStructuralFeature(partReferenceName) == null || features.contains(contextClass.getEStructuralFeature(partReferenceName)) : "Feature with the same name already exists"
	
	// metamodel adaptation
	def partClass = ePackage.newEClass(partClassName)
	def partReference = contextClass.newEReference(partReferenceName, partClass, 1, 1, true)
	partClass.eStructuralFeatures.addAll(features)
	
	// model migration
	for(contextElement in contextClass.allInstances) {
		def partElement = partClass.newInstance()
		contextElement.set(partReference, partElement);
		for(feature in features) {
			partElement.set(feature, contextElement.unset(feature))
		}
	}
	
	partClass
}

extractPartClass = extractPartClassImpl