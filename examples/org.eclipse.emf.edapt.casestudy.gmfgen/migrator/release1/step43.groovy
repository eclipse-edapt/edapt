/* --------------------------------------------------------------------------------
 * revision 1.182
 * date: 2006-12-11 18:42:43 +0000;  author: ashatalin;  lines: +3 -0;
 * [157683] - Generate link creation/initialization command in single separate classes instead of duplication of the code in different SemanticEditPolicy'ies
 * -------------------------------------------------------------------------------- */

// class GenNode
gmfgen.GenNode.newEAttribute("createCommandClassName", emf.EString, 0, 1)
gmfgen.GenNode.newEOperation("getCreateCommandQualifiedClassName", emf.EString, 0, 1)
