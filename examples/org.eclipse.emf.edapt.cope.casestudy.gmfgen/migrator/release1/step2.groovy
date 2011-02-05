/* --------------------------------------------------------------------------------
 * revision 1.141
 * date: 2006-08-25 18:32:57 +0000;  author: atikhomirov;  lines: +4 -0;
 * [124826] mgolubev - Support nodes with border items
 * -------------------------------------------------------------------------------- */

// class GenChildSideAffixedNode
gmfgen.newEClass("GenChildSideAffixedNode", [gmfgen.GenChildNode], false)
gmfgen.GenChildSideAffixedNode.newEAttribute("preferredSideName", emf.EString, 0, 1)
gmfgen.GenChildSideAffixedNode.preferredSideName.defaultValueLiteral = "NONE"
