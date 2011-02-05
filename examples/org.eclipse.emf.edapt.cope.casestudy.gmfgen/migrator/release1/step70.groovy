/* --------------------------------------------------------------------------------
 * revision 1.209
 * date: 2007-02-08 18:06:48 +0000;  author: atikhomirov;  lines: +26 -49;
 * refactor GenAuditContainer: flatten hierarchy as it fits most for templates we write
 * -------------------------------------------------------------------------------- */

extractClass(
		gmfgen.GenEditorGenerator, [gmfgen.GenEditorGenerator.audits],
		gmfgen, "GenAuditRoot", "audits")
generalizeReference(
		gmfgen.GenEditorGenerator.audits, gmfgen.GenEditorGenerator.audits.eType,
		0, gmfgen.GenEditorGenerator.audits.upperBound)

/*
 * start custom coupled transaction
 */
for(container in gmfgen.GenAuditContainer.instances.findAll({it.parentContainer == null})) {
	def generator = container.getContainer()
	
	if(generator == null) {
		def root = gmfgen.GenAuditRoot.newInstance()
		container.resource.rootInstances.add(root)
		container.resource.rootInstances.remove(container)		
		root.audits = container
	}
}
repository.model.checkConsistency()
/*
 * end custom coupled transaction
 */

flattenHierarchy(
		gmfgen.GenAuditRoot.audits,
		gmfgen.GenAuditContainer.childContainers,
		"categories"
)
deleteFeature(gmfgen.GenAuditRule._container)

rename(gmfgen.GenAuditContainer.audits, "rules")

newOppositeReference(gmfgen.GenAuditContainer.rules, "category", 1, 1)

dropOpposite(gmfgen.GenAuditContainer.rules)

newOppositeReference(gmfgen.GenAuditRule.category, "audits", 0, -1)

collectFeature(gmfgen.GenAuditContainer.rules, gmfgen.GenAuditRoot.categories)

/*
 * start custom coupled transaction
 */
gmfgen.GenAuditContainer.newEReference("path", gmfgen.GenAuditContainer, 1, -1, false, null)

for(container in gmfgen.GenAuditContainer.allInstances) {
	def current = container
	while(current.parentContainer != null) {
		current = current.parentContainer
		container.path.add(0, current)
	}
}
repository.model.checkConsistency()
/*
 * end custom coupled transaction
 */

deleteFeature(gmfgen.GenAuditContainer.parentContainer)
 
// metamodel adaptation

// class GenAuditRoot
newOppositeReference(gmfgen.GenAuditRoot.rules, "root", 1, 1)

// class GenAuditContainer
newOppositeReference(gmfgen.GenAuditRoot.categories, "root", 1, 1, false)

// class GenAuditRoot
newOppositeReference(gmfgen.GenEditorGenerator.audits, "editorGen", 1, 1, false)

// class GenAuditContainer
gmfgen.GenAuditContainer.eSuperTypes.remove(gmfgen.GenRuleContainerBase)

// class GenAuditRule
gmfgen.GenAuditRule.newEAttribute("contextSelectorLocalClassName", emf.EString, 0, 1)
gmfgen.GenAuditRule.getContextSelectorLocalClassName.delete()
gmfgen.GenAuditRule.root.changeable = false
gmfgen.GenAuditRule.id.eAnnotations[1].details[0].value = gmfgen.GenAuditRule.id.eAnnotations[1].details[0].value.replace("container.audits", "root.rules")
gmfgen.GenAuditRoot.rules.eAnnotations[0].delete()

// class GenAuditContainer
gmfgen.GenAuditContainer.eAnnotations[0].details[0].value = "Represents constraint category of emft.validation framework"

gmfgen.GenAuditContainer.path.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditContainer.path.eAnnotations[0].newEStringToStringMapEntry("documentation", "Hierarchical path of this audit container as ordered list of containers beginning with the root and ended with this container")

gmfgen.GenAuditContainer.getAllRulesToTargetContextMap.delete()
gmfgen.GenAuditContainer.getAllTargetedModelPackages.delete()
gmfgen.GenAuditContainer.hasDiagramElementRule.delete()
gmfgen.GenAuditContainer.getAllAuditContainers.delete()
gmfgen.GenAuditContainer.getAllAuditRules.delete()
gmfgen.GenAuditContainer.getPath.delete()