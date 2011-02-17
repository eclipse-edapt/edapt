/* --------------------------------------------------------------------------------
 * revision 1.208
 * date: 2007-02-07 00:37:48 +0000;  author: atikhomirov;  lines: +9 -0;
 * complete migration of plugin.xml template to xpand version - constraint providers rewritten
 * -------------------------------------------------------------------------------- */

// class GenAuditRule
gmfgen.GenAuditRule.newEAttribute("requiresConstraintAdapter", emf.EBoolean, 0, 1)
gmfgen.GenAuditRule.requiresConstraintAdapter.changeable = false
gmfgen.GenAuditRule.requiresConstraintAdapter.'volatile' = true
gmfgen.GenAuditRule.requiresConstraintAdapter.'transient' = true
gmfgen.GenAuditRule.requiresConstraintAdapter.derived = true
gmfgen.GenAuditRule.newEOperation("getConstraintAdapterQualifiedClassName", emf.EString, 0, 1)

// class GenAuditable
gmfgen.GenAuditable.newEOperation("getTargetClassModelQualifiedName", emf.EString, 0, 1)
gmfgen.GenAuditable.getTargetClassModelQualifiedName.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenAuditable.getTargetClassModelQualifiedName.eAnnotations[0].newEStringToStringMapEntry("documentation", "Consists of ecore meta-model only package names and target class simple name")
