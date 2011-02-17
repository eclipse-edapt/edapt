import org.eclipse.emf.ecore.EStructuralFeature.Settingimport org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencerimport org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edapt.migration.*
import org.eclipse.emf.edapt.migration.execution.*import org.eclipse.emf.edapt.common.*/* * Primitives for User Interaction */ choose = { Instance instance, List values, String message ->	return MigratorRegistry.getInstance().getOracle().choose(instance, values, message)}debug = { Instance instance, String message ->	MigratorRegistry.getInstance().getDebugger().debug(instance, message)}
/*
 * Primitives for Metamodel Adaptation
 */

// Metamodel query

mm = { String name -> repository.metamodel.getElement(name) }
p = { String name -> repository.metamodel.getEPackage(name) }rp = { String nsURI -> repository.metamodel.ePackages.find{p -> p.nsURI == nsURI} }
EPackage.metaClass.c = { String name -> delegate.getEClassifier(name) }
EClass.metaClass.f = { String name -> delegate.getEStructuralFeature(name) }
EClass.metaClass.o = { String name ->	delegate.eOperations.find{ it.name == name }}EClass.metaClass.getOperation = { String name, EClassifier... types ->	MetamodelUtils.getOperation(delegate, name, types)}EPackage.metaClass.getESubpackage = { String name ->
	delegate.eSubpackages.find{ it.name == name }
}
EPackage.metaClass.propertyMissing = { String name ->
	def p = delegate.getESubpackage(name)
	if(p) {
		return p
	}
	else {
		return delegate.c(name)
	}
}

EClass.metaClass.propertyMissing = { String name ->
	if(name.startsWith('_')) {
		name = name.substring(1)
	}	def f = delegate.f(name)	if(f) {		return f	}	else {		return delegate.o(name)	}
}EEnum.metaClass.propertyMissing = { String name ->	return delegate.eLiterals.find{ it.name == name }}EOperation.metaClass.propertyMissing = { String name ->	return delegate.eParameters.find{ it.name == name }}
EModelElement.metaClass.getInverse = {EReference reference ->	return EcoreUtils.getInverse(delegate, reference, repository.metamodel.ePackages)}EClass.metaClass.getESubTypes = { ->	return delegate.getInverse(emf.EClass._eSuperTypes)}EClass.metaClass.getEAllSubTypes = { ->	Collection<EClass> subTypes = new HashSet<EClass>()	for(subType in delegate.eSubTypes) {		subTypes.add(subType)		subTypes.addAll(subType.eAllSubTypes)	}	return subTypes}EModelElement.metaClass.clone = { ->	return MetamodelUtils.copy(delegate)}isConcrete = { EClass eClass ->	return !(eClass.'abstract' || eClass.'interface')}subtractSuperTypes = { EClass minuend, EClass subtrahend ->	def superTypes = []		superTypes.addAll(minuend.eAllSuperTypes)	superTypes.add(0, minuend)		superTypes.removeAll(subtrahend.eAllSuperTypes)	superTypes.remove(subtrahend)		return superTypes}subtractFeatures = { EClass minuend, EClass subtrahend ->	def features = []	def superTypes = subtractSuperTypes(minuend, subtrahend)		for(superType in superTypes) {		features.addAll(superType.eStructuralFeatures)	}		return features}
// Metamodel modificationEPackage.metaClass.newEPackage = { ->	def ePackage = EcoreFactory.eINSTANCE.createEPackage()	delegate.eSubpackages.add(ePackage)	ePackage}newEPackage = { ->	def ePackage = EcoreFactory.eINSTANCE.createEPackage()	repository.metamodel.resources[0].rootPackages.add(ePackage)	ePackage}

EPackage.metaClass.newEClass = { ->
	def eClass = EcoreFactory.eINSTANCE.createEClass()
	delegate.eClassifiers.add(eClass)
	eClass
}
EPackage.metaClass.newEEnum = { ->	def eEnum = EcoreFactory.eINSTANCE.createEEnum()	delegate.eClassifiers.add(eEnum)	eEnum}EEnum.metaClass.newEEnumLiteral = { ->	def eEnumLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral()	delegate.eLiterals.add(eEnumLiteral)	eEnumLiteral}EClass.metaClass.newEAttribute = { ->
	def eAttribute = EcoreFactory.eINSTANCE.createEAttribute()
	delegate.eStructuralFeatures.add(eAttribute)
	eAttribute
}
EClass.metaClass.newEReference = { ->
	def eReference = EcoreFactory.eINSTANCE.createEReference()
	delegate.eStructuralFeatures.add(eReference)
	eReference
}
EClass.metaClass.newEOperation = { ->	def eOperation = EcoreFactory.eINSTANCE.createEOperation()	delegate.eOperations.add(eOperation)	eOperation}EClass.metaClass.newEGenericType = { ->	def eGenericType = EcoreFactory.eINSTANCE.createEGenericType()	delegate.eGenericSuperTypes.add(eGenericType)	eGenericType}EModelElement.metaClass.newEAnnotation = { ->
	def eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation()
	delegate.eAnnotations.add(eAnnotation)
	eAnnotation
}EAnnotation.metaClass.newEStringToStringMapEntry = { ->	def entry = EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry())	delegate.details.add(entry)	entry}ETypedElement.metaClass.newEGenericType = { ->	def eGenericType = EcoreFactory.eINSTANCE.createEGenericType()	delegate.eGenericType = eGenericType	eGenericType}EOperation.metaClass.newEParameter = { ->	def eParameter = EcoreFactory.eINSTANCE.createEParameter()	delegate.eParameters.add(eParameter)	eParameter}EModelElement.metaClass.delete = { -> EcoreUtil.delete(delegate) }EGenericType.metaClass.delete = { -> EcoreUtil.delete(delegate) }EReference.metaClass.setEOpposite = {EReference opposite ->	repository.model.setEOpposite(delegate, opposite)}EPackage.metaClass.newEClass = {String name, Collection<EClass> superClasses, boolean abstr ->	MetamodelUtils.newEClass(delegate, name, superClasses, abstr)}EPackage.metaClass.newEClass = {String name, Collection<EClass> superClasses ->	MetamodelUtils.newEClass(delegate, name, superClasses, false)}EPackage.metaClass.newEClass = {String name ->	MetamodelUtils.newEClass(delegate, name, [], false)}EPackage.metaClass.newEEnum = {String name ->	MetamodelUtils.newEEnum(delegate, name)}EEnum.metaClass.newEEnumLiteral = {String name ->	MetamodelUtils.newEEnumLiteral(delegate, name)}EClass.metaClass.newEReference = {String name, EClass type, int lowerBound, int upperBound, boolean containment, EReference opposite ->	def reference = MetamodelUtils.newEReference(delegate, name, type, lowerBound, upperBound, containment)	if(opposite != null) {		opposite.eOpposite = reference	}	reference}EClass.metaClass.newEReference = {String name, EClass type, int lowerBound, int upperBound, boolean containment ->	MetamodelUtils.newEReference(delegate, name, type, lowerBound, upperBound, containment)}EClass.metaClass.newEReference = {String name, EClass type, int lowerBound, int upperBound ->	MetamodelUtils.newEReference(delegate, name, type, lowerBound, upperBound, false)}EClass.metaClass.newEReference = {String name, EClass type, int lowerBound ->	MetamodelUtils.newEReference(delegate, name, type, lowerBound, 1, false)}EClass.metaClass.newEReference = {String name, EClass type ->	MetamodelUtils.newEReference(delegate, name, type, 0, 1, false)}EClass.metaClass.newEAttribute = {String name, EDataType type, int lowerBound, int upperBound, String defaultValue ->	MetamodelUtils.newEAttribute(delegate, name, type, lowerBound, upperBound, defaultValue)}EClass.metaClass.newEAttribute = {String name, EDataType type, int lowerBound, int upperBound ->	MetamodelUtils.newEAttribute(delegate, name, type, lowerBound, upperBound, null)}EClass.metaClass.newEAttribute = {String name, EDataType type, int lowerBound ->	MetamodelUtils.newEAttribute(delegate, name, type, lowerBound, 1, null)}EClass.metaClass.newEAttribute = {String name, EDataType type ->	MetamodelUtils.newEAttribute(delegate, name, type, 0, 1, null)}EClass.metaClass.newEOperation = {String name, EClassifier type, int lowerBound, int upperBound ->	MetamodelUtils.newEOperation(delegate, name, type, lowerBound, upperBound)}EOperation.metaClass.newEParameter = {String name, EClassifier type, int lowerBound, int upperBound ->	MetamodelUtils.newEParameter(delegate, name, type, lowerBound, upperBound)}EModelElement.metaClass.newEAnnotation = {String source ->	MetamodelUtils.newEAnnotation(delegate, source)}EAnnotation.metaClass.newEStringToStringMapEntry = {String key, String value ->	MetamodelUtils.newEStringToStringMapEntry(delegate, key, value)}
/**
 * Primitives for Model Migration
 */
 
// Model query

Instance.metaClass.propertyMissing = { String name ->
	if(name.startsWith('_')) {
		name = name.substring(1)
	}
	delegate.get(name)
}Model.metaClass.checkConsistency = { -> delegate.checkConformance() }

EClass.metaClass.getInstances = { -> repository.model.getInstances(delegate) }
EClass.metaClass.getAllInstances = { -> repository.model.getAllInstances(delegate) }

// Model modification

Instance.metaClass.setProperty = { String name, Object value ->
	if(name.startsWith('_')) {
		name = name.substring(1)
	}
	delegate.set(name, value)
}

EClass.metaClass.newInstance = { -> repository.model.newInstance(delegate) }

Instance.metaClass.delete = {
	repository.model.delete(delegate)
}deleteFeatureValue = {Instance instance, EStructuralFeature feature ->	def value = instance.unset(feature)	if(feature instanceof EReference && feature.containment) {		if(feature.many) {			for(v in value) {				v.delete()			}		}		else if(value != null) {			value.delete()		}	}}//copy a list of instancesInstance.metaClass.clone = { ->	// copy the tree structure with an instance as root	copyTree = {Instance original, Map<Instance,Instance> map ->		def EClass eClass = original.eClass		def Instance copi = eClass.newInstance()		for(EReference reference in eClass.getEAllReferences()) {			if(reference.containment) {				if(reference.many) {					for(Instance child in original.get(reference)) {						copi.get(reference).add(copyTree(child, map))					}				}				else {					def Instance child = original.get(reference)					if(child != null) {						copi.set(reference, copyTree(child, map))					}				}			}		}		for(EAttribute attribute in eClass.getEAllAttributes()) {			copi.set(attribute, original.get(attribute))		}		map[original] = copi		return copi	}		// copy cross references of an instance	copyReferences = {Instance original, Map<Instance,Instance> map ->		def EClass eClass = original.eClass		def Instance copi = map[original]		for(EReference reference in eClass.getEAllReferences()) {			if(!reference.containment) {				if(reference.many) {					if(reference.eOpposite == null || reference.eOpposite.many) {						for(Instance ref in original.get(reference)) {							if(map[ref] != null) {								ref = map[ref]							}							copi.get(reference).add(ref)						}					}				}				else {					if(reference.eOpposite == null || !reference.eOpposite.containment) {						def Instance ref = original.get(reference)						if(ref != null) {							if(map[ref] != null) {								ref = map[ref]							}							copi.set(reference, ref)						}					}				}			}		}	}	def original = delegate	// mapping of originals to copies	Map<Instance,Instance> map = new HashMap<Instance,Instance>()		// copy tree structure	def copy = copyTree(original, map)		// copy cross references	copyReferences(original, map)	return copy}