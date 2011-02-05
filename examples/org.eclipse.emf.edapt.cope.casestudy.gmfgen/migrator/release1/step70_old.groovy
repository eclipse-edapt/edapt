/* --------------------------------------------------------------------------------
 * revision 1.209
 * date: 2007-02-08 18:06:48 +0000;  author: atikhomirov;  lines: +26 -49;
 * refactor GenAuditContainer: flatten hierarchy as it fits most for templates we write
 * -------------------------------------------------------------------------------- */

/*
 * start custom coupled transaction
 */

// metamodel adaptation

// class GenAuditRoot
gmfgen.newEClass("GenAuditRoot")

gmfgen.GenAuditRoot.newEReference("categories", gmfgen.GenAuditContainer, 0, -1, true)
gmfgen.GenAuditRoot.newEReference("rules", gmfgen.GenAuditRule, 0, -1, true, null)
gmfgen.GenAuditRule.newEReference("root", gmfgen.GenAuditRoot, 1, 1, false, gmfgen.GenAuditRoot.rules)

// class GenAuditContainer
gmfgen.GenAuditContainer.newEReference("root", gmfgen.GenAuditRoot, 1, 1, false, gmfgen.GenAuditRoot.categories)
gmfgen.GenAuditContainer.root.changeable = false
gmfgen.GenAuditContainer.audits.containment = false
gmfgen.GenAuditContainer.newEReference("path", gmfgen.GenAuditContainer, 1, -1, false, null)

// class GenAuditRule
rename(gmfgen.GenAuditRule._container, "category")

// class GenEditorGenerator
gmfgen.GenEditorGenerator.audits.eType = gmfgen.GenAuditRoot
gmfgen.GenEditorGenerator.audits.lowerBound = 0


// model migration
doContainer = {parent, root ->
	root.rules.addAll(parent.audits)
	for(child in parent.childContainers) {
		child.path.addAll(parent.path)
		child.path.add(parent)
		doContainer(child, root)
	}
	root.categories.addAll(parent.unset(gmfgen.GenAuditContainer.childContainers))
}

for(container in gmfgen.GenAuditContainer.instances.findAll({it.parentContainer == null})) {
	def root = gmfgen.GenAuditRoot.newInstance()
	def generator = container.getContainer()
	
	if(generator == null) {
		container.resource.rootInstances.add(root)
		container.resource.rootInstances.remove(container)		
		root.categories.add(container)
		
		doContainer(container, root)
	}
	else if(container.id != null || container.name != null || container.description != null) {
		generator.audits = root
		generator.resource.rootInstances.remove(container)
		root.categories.add(container)
		
		doContainer(container, root)
	}
	else {
		generator.audits = root
		root.categories.addAll(container.unset(gmfgen.GenAuditContainer.childContainers))
		root.rules.addAll(container.unset(gmfgen.GenAuditContainer.audits))
		container.delete()
		
		for(child in new ArrayList(root.categories)) {
			doContainer(child, root)				
		}
	}
}

// class GenAuditRoot
gmfgen.GenAuditRoot.newEReference("editorGen", gmfgen.GenEditorGenerator, 1, 1, false, gmfgen.GenEditorGenerator.audits)
gmfgen.GenAuditRoot.editorGen.changeable = false

// class GenAuditContainer
gmfgen.GenAuditContainer.parentContainer.delete()
gmfgen.GenAuditContainer.childContainers.delete()
gmfgen.GenAuditContainer.eSuperTypes.remove(gmfgen.GenRuleContainerBase)

// class GenAuditRule
gmfgen.GenAuditRule.newEAttribute("contextSelectorLocalClassName", emf.EString, 0, 1)
gmfgen.GenAuditRule.getContextSelectorLocalClassName.delete()
gmfgen.GenAuditRule.root.changeable = false
gmfgen.GenAuditRule.id.eAnnotations[1].details[0].value = gmfgen.GenAuditRule.id.eAnnotations[1].details[0].value.replace("container.audits", "root.rules")
gmfgen.GenAuditRule.category.eAnnotations[0].delete()

// class GenAuditContainer
gmfgen.GenAuditContainer.eAnnotations[0].details[0].value = "Represents constraint category of emft.validation framework"
gmfgen.GenAuditContainer.audits.eAnnotations[0].details[0].value = "Hierarchical path of this audit container as ordered list of containers beginning with the root and ended with this container"
gmfgen.GenAuditContainer.path.eAnnotations.add(gmfgen.GenAuditContainer.audits.eAnnotations[0])

gmfgen.GenAuditContainer.getAllRulesToTargetContextMap.delete()
gmfgen.GenAuditContainer.getAllTargetedModelPackages.delete()
gmfgen.GenAuditContainer.hasDiagramElementRule.delete()
gmfgen.GenAuditContainer.getAllAuditContainers.delete()
gmfgen.GenAuditContainer.getAllAuditRules.delete()
gmfgen.GenAuditContainer.getPath.delete()

/*
 * end custom coupled transaction
 */
