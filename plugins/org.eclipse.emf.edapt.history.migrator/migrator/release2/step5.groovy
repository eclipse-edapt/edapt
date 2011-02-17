import org.eclipse.emf.ecore.*
import org.eclipse.emf.ecore.util.*

defaultPackage = history

// mapping between current and reconstructed metamodel
def root = []

def forwardMapping = [:]
getForward = { instance ->
	return forwardMapping[instance]
}
resolveForward = { instance ->
	def forward = getForward(instance)
	if(forward == null) {
		return instance
	}
	return forward
}

def backwardMapping = [:]
getBackward = { instance ->
	return backwardMapping[instance]
}

map = { source, target ->
	forwardMapping[source] = target
	backwardMapping[target] = source
}

// reconstruction
visitValueChange = { change ->
	if(change.instanceOf(history.Set)) {
		visitSet(change)
	}
	else if(change.instanceOf(Add)) {
		visitAdd(change)
	}
	else if(change.instanceOf(Remove)) {
		visitRemove(change)
	}
}

visitContentChange = { change ->
	if(change.instanceOf(Create)) {
		visitCreate(change)
	}
	else if(change.instanceOf(Move)) {
		visitMove(change)
	}
	else if(change.instanceOf(Delete)){
		visitDelete(change)
	}
}

visitCreate = { change ->
	def element = change.element.eClass.newInstance()
	map(change.element, element)
	if(change.target != null) {
		def reference = change.target.eClass.getEStructuralFeature(change.referenceName)
		getForward(change.target).get(reference).add(element)
	}
	else {
		root.add(element)
	}
	for(child in change.changes) {
		visitValueChange(child)
	}
}

visitMove = { change ->
	// make bidirectional
	change.source = getBackward(getForward(change.element).getContainer())
	// apply change
	def reference = change.target.eClass.getEStructuralFeature(change.referenceName)
	getForward(change.element).getContainer().get(reference).remove(getForward(change.element))
	getForward(change.target).get(reference).add(getForward(change.element))
}

contains = { containee, container ->
	if(containee == null) {
		return false;
	}
	if(containee == container) {
		return true;
	}
	return contains(containee.container, container)
}

addRemoves = { element, delete ->
	for(referenceSlot in element.references) {
		def source = referenceSlot.instance
		if(contains(source, element) || contains(element, source)) {
			continue;
		}
		def remove = Remove.newInstance()
		remove.element = getBackward(source)
		remove.featureName = referenceSlot.eReference.name
		remove.referenceValue = getBackward(element)
		delete.changes.add(remove)
	}
	for(child in element.contents) {
		addRemoves(child, delete)
	}
}

visitDelete = { change ->
	// make bidirectional
	addRemoves(getForward(change.element), change)
	def element = getForward(change.element)
	change.target = getBackward(element.container)
	change.referenceName = element.containerReference.name
	// apply change
	element.delete()
}

visitSet = { change ->
	def feature = change.element.eClass.getEStructuralFeature(change.featureName)
	if(feature instanceof EReference) {
		// make bidirectional
		change.oldReferenceValue = getBackward(getForward(change.element).get(feature))
		// apply change
		getForward(change.element).set(feature, resolveForward(change.referenceValue))
	}
	else {
		// make bidirectional
		change.oldDataValue = EcoreUtil.convertToString(feature.eType, getForward(change.element).get(feature))
		// apply change
		getForward(change.element).set(feature, EcoreUtil.createFromString(feature.eType, change.dataValue))		
	}
}

visitAdd = { change ->
	def feature = change.element.eClass.getEStructuralFeature(change.featureName)
	if(feature instanceof EReference) {
		// apply change
		getForward(change.element).add(feature, resolveForward(change.referenceValue))
	}
	else {
		// apply change
		getForward(change.element).add(feature, EcoreUtil.createFromString(feature.eType, change.dataValue))		
	}
}

visitRemove = { change ->
	def feature = change.element.eClass.getEStructuralFeature(change.featureName)
	if(feature instanceof EReference) {
		// apply change
		getForward(change.element).remove(feature, resolveForward(change.referenceValue))
	}
	else {
		// apply change
		getForward(change.element).remove(feature, EcoreUtil.createFromString(feature.eType, change.dataValue))		
	}
}

visitChange = { change ->
	// CompositeChange
	if(change.instanceOf(CompositeChange)) {
		for(child in change.changes) {
			visitChange(child)
		}
	}
	// MigrationChange
	else if(change.instanceOf(MigrationChange)) {
		for(child in change.changes) {
			visitChange(child)
		}
	}
	// ContentChange
	else if(change.instanceOf(ContentChange)) {
		visitContentChange(change)
	}
	// ValueChange
	else if(change.instanceOf(ValueChange)) {
		visitValueChange(change)
	}
	// NoChange
	else if(change.instanceOf(NoChange)) {
		// ignore
	}
	else {
		println change.eClass.name
		assert false
	}
}

def h = History.instances[0]

for(release in h.releases) {
	for(change in release.changes) {
		visitChange(change)
	}
}

// cleanup
for(r in root) {
	r.delete()
}