/* --------------------------------------------------------------------------------
 * revision 1.214
 * date: 2007-03-20 20:15:02 +0000;  author: ashatalin;  lines: +4 -1;
 * NavigatorReference type constant name corrected.
 * Generating wrapper for domain model navigator nodes to preserve navigator tree selection/expansion on model files reload.
 * -------------------------------------------------------------------------------- */

// class GenDomainModelNavigator
gmfgen.GenDomainModelNavigator.newEAttribute("domainNavigatorItemClassName", emf.EString, 0, 1)
gmfgen.GenDomainModelNavigator.newEOperation("getDomainNavigatorItemQualifiedClassName", emf.EString, 0, 1)

// enum GenNavigatorReferenceType
rename(gmfgen.GenNavigatorReferenceType.out_taget, "out_target")
