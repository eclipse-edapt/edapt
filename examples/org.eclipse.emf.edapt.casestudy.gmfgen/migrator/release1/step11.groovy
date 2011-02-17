/* --------------------------------------------------------------------------------
 * revision 1.150
 * date: 2006-09-22 13:21:25 +0000;  author: ashatalin;  lines: +15 -2;
 * Read-only properties provided into the navigator.
 * -------------------------------------------------------------------------------- */

// enum GeneratedType
gmfgen.newEEnum("GeneratedType")
gmfgen.GeneratedType.newEEnumLiteral("abstractNavigatorItem")
gmfgen.GeneratedType.abstractNavigatorItem.value = 0
gmfgen.GeneratedType.abstractNavigatorItem.instance = gmfgen.GeneratedType.abstractNavigatorItem
gmfgen.GeneratedType.abstractNavigatorItem.literal = "abstractNavigatorItem"

gmfgen.TypeTabFilter.newEAttribute("generatedTypes", gmfgen.GeneratedType, 0, -1)
gmfgen.TypeTabFilter.newEOperation("getAllTypes", emf.EString, 1, -1)

// class GenNavigator
gmfgen.GenNavigator.newEAttribute("abstractNavigatorItemClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEAttribute("navigatorItemClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEAttribute("packageName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEOperation("getAbstractNavigatorItemQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEOperation("getNavigatorItemQualifiedClassName", emf.EString, 0, 1)

rename(gmfgen.GenNavigator.groupWrapperClassName, "navigatorGroupClassName")
rename(gmfgen.GenNavigator.getGroupWrapperQualifiedClassName, "getNavigatorGroupQualifiedClassName")
