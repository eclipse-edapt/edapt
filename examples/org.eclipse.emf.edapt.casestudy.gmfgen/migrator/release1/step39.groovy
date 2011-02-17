/* --------------------------------------------------------------------------------
 * revision 1.178
 * date: 2006-11-28 19:35:55 +0000;  author: ashatalin;  lines: +7 -2;
 * [157683] - Generate link creation/initialization command in single separate classes instead of duplication of the code in different SemanticEditPolicy'ies
 * -------------------------------------------------------------------------------- */

// class ToolGroupItem
generalizeReference(gmfgen.ToolGroupItem.group, gmfgen.ToolGroupItem.group.eType, 0, 1)

// class FeatureLinkModelFacet
gmfgen.FeatureLinkModelFacet.metaFeature.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
gmfgen.FeatureLinkModelFacet.metaFeature.eAnnotations[0].newEStringToStringMapEntry("ocl", "metaFeature.ecoreFeature.unique")
gmfgen.FeatureLinkModelFacet.metaFeature.eAnnotations[0].newEStringToStringMapEntry("description", "All references are unique in EMF due to the current code generation")
