/* --------------------------------------------------------------------------------
 * revision 1.192
 * date: 2006-12-28 19:45:50 +0000;  author: ashatalin;  lines: +20 -1;
 * NavigatorContentProvider template reimplemented on xpand
 * -------------------------------------------------------------------------------- */

// class GenNavigatorPathSegment
gmfgen.newEClass("GenNavigatorPathSegment", [], false)
gmfgen.GenNavigatorPathSegment.newEReference("from", gmfgen.GenCommonBase, 1, 1, false, null)
gmfgen.GenNavigatorPathSegment.newEReference("to", gmfgen.GenCommonBase, 1, 1, false, null)

//class GenNavigatorPath
gmfgen.newEClass("GenNavigatorPath", [], false)
gmfgen.GenNavigatorPath.newEReference("segments", gmfgen.GenNavigatorPathSegment, 0, -1, true, null)

gmfgen.GenNavigatorPathSegment.newEReference("path", gmfgen.GenNavigatorPath, 0, 1, false, gmfgen.GenNavigatorPath.segments)
gmfgen.GenNavigatorPathSegment.path.changeable = false

// class GenNavigator
gmfgen.GenNavigator.getChildReferencesFrom.newEAnnotation("http://www.eclipse.org/gmf/2006/deprecated")
gmfgen.GenNavigator.getChildReferencesFrom.eAnnotations[0].newEStringToStringMapEntry("documentation", "corresponding method should be implemented in .ext file")
gmfgen.GenNavigator.getChildReferencesTo.newEAnnotation("http://www.eclipse.org/gmf/2006/deprecated")
gmfgen.GenNavigator.getChildReferencesTo.eAnnotations[0].newEStringToStringMapEntry("documentation", "corresponding method should be implemented in .ext file")

// class GenNavigatorChildReference
newOppositeReference(gmfgen.GenNavigator.childReferences, "navigator", 0, 1, false)
gmfgen.GenNavigatorChildReference.newEOperation("findConnectionPaths", gmfgen.GenNavigatorPath, 0, -1)
