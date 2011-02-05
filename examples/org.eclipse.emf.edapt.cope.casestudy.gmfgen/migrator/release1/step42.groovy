/* --------------------------------------------------------------------------------
 * revision 1.181
 * date: 2006-12-06 18:12:46 +0000;  author: ashatalin;  lines: +3 -0;
 * [157683] - Generate link creation/initialization command in single separate classes instead of duplication of the code in different SemanticEditPolicy'ies
 * -------------------------------------------------------------------------------- */

// class LinkConstraints
gmfgen.LinkConstraints.hasLinkCreationConstraints.newEAnnotation("http://www.eclipse.org/gmf/2006/deprecated")
gmfgen.LinkConstraints.hasLinkCreationConstraints.eAnnotations[1].newEStringToStringMapEntry("documentation", "LinkCreationConstants should be generated if diagram has any links")
