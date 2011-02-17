/* --------------------------------------------------------------------------------
 * revision 1.243
 * date: 2008-04-14 08:38:03 +0000;  author: atikhomirov;  lines: +30 -9;
 * [226149] Refactor Validation/Constraints: explicit context groupings instead of ID matching, unused IClientSelectors implementations are no longer generated, less custom code in the model, odd approach with map (semanticCtxIdMap) replaced with in-place VisualID check.
 * -------------------------------------------------------------------------------- */

extractAndGroupAttribute(
	gmfgen.GenAuditRule.contextSelectorLocalClassName,
	gmfgen,
	"GenAuditContext",
	"contextSelector",
	gmfgen.GenAuditRoot,
	"clientContexts"
)
rename(gmfgen.GenAuditContext.contextSelectorLocalClassName, "className")

moveFeature(
		gmfgen.GenAuditRule.contextSelector,
		gmfgen.GenAuditRule.target)

// class GenAuditRoot
newOppositeReference(gmfgen.GenAuditRoot.clientContexts, "root", 1, 1, true)
gmfgen.GenAuditContext.root.resolveProxies = false

/*
 * start custom coupled transaction
 */
gmfgen.GenAuditContext.newEAttribute("id", emf.EString, 1, 1)
gmfgen.GenAuditContext.id.defaultValueLiteral = "DefaultCtx"
gmfgen.GenAuditContext.id.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditContext.id.eAnnotations[0].newEStringToStringMapEntry("documentation", "Identifier of the validation client context for this target. Should be a valid java identifier. Identifier will be qualified automatically (i.e. should be unique within this editor only).")

generateUnique = {context ->
	def id = context.className
	int i = 0
	boolean haveSuchId = true
	while(haveSuchId) {
		haveSuchId = false
		for(next in context.root.clientContexts) {
			if(id.equals(next.id)) {
				haveSuchId = true
				id = defaultId + (++i)
				break
			}
		}
	}
	return id
}
 
for(context in gmfgen.GenAuditContext.allInstances) {
	context.id = generateUnique(context)
}

repository.model.checkConsistency()
/*
 * end custom coupled transaction
 */
 

// class GenAuditContext
gmfgen.GenAuditContext.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditContext.eAnnotations[0].newEStringToStringMapEntry("documentation", "")
gmfgen.GenAuditContext.newEOperation("getQualifiedClassName", emf.EString, 1, 1)
gmfgen.GenAuditContext.className.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditContext.className.eAnnotations[0].newEStringToStringMapEntry("documentation", "Unless explicitly set, equals to 'id'. Note, this class may get generated as inner class.")

// class GenAuditable
gmfgen.GenAuditable.contextSelector.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditable.contextSelector.eAnnotations[0].newEStringToStringMapEntry("documentation", "To apply audit to this target, we need to select appropriate input, and here's selector that helps with that")
gmfgen.GenAuditable.getClientContextID.delete()
newOppositeReference(gmfgen.GenAuditable.contextSelector, "ruleTargets", 0, -1)

// class GenAuditRule
gmfgen.GenAuditRule.getContextSelectorQualifiedClassName.delete()
gmfgen.GenAuditRule.getContextSelectorClassName.delete()