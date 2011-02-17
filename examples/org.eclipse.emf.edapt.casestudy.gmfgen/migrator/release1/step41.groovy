/* --------------------------------------------------------------------------------
 * revision 1.180
 * date: 2006-11-30 18:19:30 +0000;  author: ashatalin;  lines: +4 -0;
 * [157683] - Generate link creation/initialization command in single separate classes instead of duplication of the code in different SemanticEditPolicy'ies
 * -------------------------------------------------------------------------------- */

// class GenLink
gmfgen.GenLink.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
gmfgen.GenLink.eAnnotations[1].newEStringToStringMapEntry("ocl", "outgoingCreationAllowed or incomingCreationAllowed")
gmfgen.GenLink.eAnnotations[1].newEStringToStringMapEntry("description", "Either outgoingCreationAllowed or incomingCreationAllowed property should be true")
