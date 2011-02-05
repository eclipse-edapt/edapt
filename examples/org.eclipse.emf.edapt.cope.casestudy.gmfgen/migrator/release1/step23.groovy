/* --------------------------------------------------------------------------------
 * revision 1.162
 * date: 2006-10-17 19:19:54 +0000;  author: ashatalin;  lines: +4 -0;
 * Open action for diagram nodes in Navigator added.
 * -------------------------------------------------------------------------------- */

// class GenNavigator
gmfgen.GenNavigator.newEAttribute("actionProviderID", emf.EString, 0, 1)
gmfgen.GenNavigator.newEAttribute("actionProviderClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEOperation("getActionProviderQualifiedClassName", emf.EString, 0, 1)
