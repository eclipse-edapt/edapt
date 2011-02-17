/* --------------------------------------------------------------------------------
 * revision 1.243
 * date: 2008-04-14 08:38:03 +0000;  author: atikhomirov;  lines: +30 -9;
 * [226149] Refactor Validation/Constraints: explicit context groupings instead of ID matching, unused IClientSelectors implementations are no longer generated, less custom code in the model, odd approach with map (semanticCtxIdMap) replaced with in-place VisualID check.
 * -------------------------------------------------------------------------------- */

/*
 * start custom coupled transaction
 */

// class GenAuditContext
gmfgen.newEClass("GenAuditContext", [], false)
gmfgen.GenAuditContext.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditContext.eAnnotations[0].newEStringToStringMapEntry("documentation", "")
gmfgen.GenAuditContext.newEOperation("getQualifiedClassName", emf.EString, 1, 1)
gmfgen.GenAuditContext.newEAttribute("id", emf.EString, 1, 1)
gmfgen.GenAuditContext.id.defaultValueLiteral = "DefaultCtx"
gmfgen.GenAuditContext.id.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditContext.id.eAnnotations[0].newEStringToStringMapEntry("documentation", "Identifier of the validation client context for this target. Should be a valid java identifier. Identifier will be qualified automatically (i.e. should be unique within this editor only).")
gmfgen.GenAuditContext.newEAttribute("className", emf.EString, 1, 1)
gmfgen.GenAuditContext.className.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditContext.className.eAnnotations[0].newEStringToStringMapEntry("documentation", "Unless explicitly set, equals to 'id'. Note, this class may get generated as inner class.")

// class GenAuditRoot
gmfgen.GenAuditRoot.newEReference("clientContexts", gmfgen.GenAuditContext, 0, -1, true, null)
gmfgen.GenAuditContext.newEReference("root", gmfgen.GenAuditRoot, 1, 1, false, gmfgen.GenAuditRoot.clientContexts)
gmfgen.GenAuditContext.root.resolveProxies = false

// class GenAuditable
gmfgen.GenAuditable.newEReference("contextSelector", gmfgen.GenAuditContext, 0, 1, false, null)
gmfgen.GenAuditable.contextSelector.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditable.contextSelector.eAnnotations[0].newEStringToStringMapEntry("documentation", "To apply audit to this target, we need to select appropriate input, and here's selector that helps with that")
gmfgen.GenAuditable.getClientContextID.delete()
gmfgen.GenAuditContext.newEReference("ruleTargets", gmfgen.GenAuditable, 0, -1, false, gmfgen.GenAuditable.contextSelector)

// class GenAuditRule
gmfgen.GenAuditRule.getContextSelectorQualifiedClassName.delete()
gmfgen.GenAuditRule.getContextSelectorClassName.delete()

generateUnique = {root, defaultId ->
	def id = defaultId == null ? "" : defaultId
	int i = 0
	boolean haveSuchId = true
	while(haveSuchId) {
		haveSuchId = false
		for(next in root.clientContexts) {
			if(id.equals(next.id)) {
				haveSuchId = true
				id = defaultId + (++i)
				break
			}
		}
	}
	return id
}

getOrCreateContext = {root, className ->
	def context = null
	for(next in root.clientContexts) {
		def explicit = next.className
		if(className == explicit || (className != null && className.equals(explicit)) || className.equals(next.id)) {
			context = next
			break
		}
	}
	if(context == null) {
		context = gmfgen.GenAuditContext.newInstance()
		String id = generateUnique(root, className)
		context.id = id
		if(!id.equals(className)) {
			context.className = className	
		}
		root.clientContexts.add(context)
	}
	return context
}

for(rule in gmfgen.GenAuditRule.allInstances) {
	if(rule.contextSelectorLocalClassName == null) {
		continue;
	}
	def context = getOrCreateContext(rule.root, rule.contextSelectorLocalClassName)
	if(rule.target != null) {
		rule.target.contextSelector = context
	}
	rule.contextSelectorLocalClassName = null
}

// class GenAuditRule
gmfgen.GenAuditRule.contextSelectorLocalClassName.delete()
/*
 * end custom coupled transaction
 */
