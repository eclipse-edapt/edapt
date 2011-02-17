/* --------------------------------------------------------------------------------
 * revision 1.176
 * date: 2006-11-24 22:25:21 +0000;  author: ashatalin;  lines: +3 -0;
 * [157683] - Generate link creation/initialization command in single separate classes instead of duplication of the code in different SemanticEditPolicy'ies
 * -------------------------------------------------------------------------------- */

// class TypeLinkModelFacet
gmfgen.TypeLinkModelFacet.newEAttribute("createCommandClassName", emf.EString, 0, 1)
gmfgen.TypeLinkModelFacet.newEOperation("getCreateCommandQualifiedClassName", emf.EString, 0, 1)
