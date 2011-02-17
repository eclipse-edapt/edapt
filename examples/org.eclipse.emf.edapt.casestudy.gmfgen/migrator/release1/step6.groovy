/* --------------------------------------------------------------------------------
 * revision 1.145
 * date: 2006-09-12 18:18:19 +0000;  author: ashatalin;  lines: +2 -0;
 * - hideIfEmpty property added to the GenNavigatorChildReference
 * - recursive iteration through connections graph added
 * -------------------------------------------------------------------------------- */

// attribute GenNavigatorChildReference ---label: Boolean
gmfgen.GenNavigatorChildReference.newEAttribute("hideIfEmpty", emf.EBoolean, 0, 1)
gmfgen.GenNavigatorChildReference.hideIfEmpty.defaultValueLiteral = "true"
