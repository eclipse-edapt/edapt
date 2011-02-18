import org.eclipse.emf.ecore.EStructuralFeature.Setting
import org.eclipse.emf.ecore.util.*
import org.eclipse.emf.edapt.migration.*
import org.eclipse.emf.edapt.migration.execution.*
/*
 * Primitives for Metamodel Adaptation
 */

// Metamodel query

mm = { String name -> repository.metamodel.getElement(name) }
p = { String name -> repository.metamodel.getEPackage(name) }
EPackage.metaClass.c = { String name -> delegate.getEClassifier(name) }
EClass.metaClass.f = { String name -> delegate.getEStructuralFeature(name) }
EClass.metaClass.o = { String name ->
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
	}
}

// Metamodel modification

EPackage.metaClass.newEClass = { ->
	def eClass = EcoreFactory.eINSTANCE.createEClass()
	delegate.eClassifiers.add(eClass)
	eClass
}
EPackage.metaClass.newEEnum = { ->
	def eAttribute = EcoreFactory.eINSTANCE.createEAttribute()
	delegate.eStructuralFeatures.add(eAttribute)
	eAttribute
}
EClass.metaClass.newEReference = { ->
	def eReference = EcoreFactory.eINSTANCE.createEReference()
	delegate.eStructuralFeatures.add(eReference)
	eReference
}
EClass.metaClass.newEOperation = { ->
	def eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation()
	delegate.eAnnotations.add(eAnnotation)
	eAnnotation
}
/**
 * Primitives for Model Migration
 */
 
// Model query

Instance.metaClass.propertyMissing = { String name ->
	if(name.startsWith('_')) {
		name = name.substring(1)
	}
	delegate.get(name)
}

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
}