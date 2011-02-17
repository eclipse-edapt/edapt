/* --------------------------------------------------------------------------------
 * revision 1.143
 * date: 2006-09-11 18:21:36 +0000;  author: ashatalin;  lines: +40 -0;
 * Model navigator, initial version.
 * -------------------------------------------------------------------------------- */

// class GenNavigatorReferenceType
gmfgen.newEEnum("GenNavigatorReferenceType")
gmfgen.GenNavigatorReferenceType.newEEnumLiteral("children")
gmfgen.GenNavigatorReferenceType.children.value = 0
gmfgen.GenNavigatorReferenceType.children.instance = gmfgen.GenNavigatorReferenceType.children
gmfgen.GenNavigatorReferenceType.children.literal = "children"
gmfgen.GenNavigatorReferenceType.newEEnumLiteral("out_taget")
gmfgen.GenNavigatorReferenceType.out_taget.value = 1
gmfgen.GenNavigatorReferenceType.out_taget.instance = gmfgen.GenNavigatorReferenceType.out_taget
gmfgen.GenNavigatorReferenceType.out_taget.literal = "out_taget"
gmfgen.GenNavigatorReferenceType.newEEnumLiteral("in_source")
gmfgen.GenNavigatorReferenceType.in_source.value = 2
gmfgen.GenNavigatorReferenceType.in_source.instance = gmfgen.GenNavigatorReferenceType.in_source
gmfgen.GenNavigatorReferenceType.in_source.literal = "in_source"
gmfgen.GenNavigatorReferenceType.newEEnumLiteral("default")
gmfgen.GenNavigatorReferenceType.default.value = 3
gmfgen.GenNavigatorReferenceType.default.instance = gmfgen.GenNavigatorReferenceType.default
gmfgen.GenNavigatorReferenceType.default.literal = "default"

// class GenNavigatorChildReference
gmfgen.newEClass("GenNavigatorChildReference", [], false)
gmfgen.GenNavigatorChildReference.newEAttribute("groupIcon", emf.EString, 0, 1)
gmfgen.GenNavigatorChildReference.newEAttribute("referenceType", gmfgen.GenNavigatorReferenceType, 1, 1)
gmfgen.GenNavigatorChildReference.newEAttribute("groupName", emf.EString, 0, 1)
gmfgen.GenNavigatorChildReference.newEReference("parent", gmfgen.GenCommonBase, 0, 1, false, null)
gmfgen.GenNavigatorChildReference.newEReference("child", gmfgen.GenCommonBase, 1, 1, false, null)
gmfgen.GenNavigatorChildReference.newEReference("label", gmfgen.GenLabel, 0, 1, false, null)
gmfgen.GenNavigatorChildReference.newEOperation("isInsideGroup", emf.EBoolean, 0, 1)

// class GenNavigator
gmfgen.newEClass("GenNavigator", [], false)
gmfgen.GenNavigator.newEAttribute("contentExtensionID", emf.EString, 0, 1)
gmfgen.GenNavigator.newEAttribute("contentExtensionName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEAttribute("contentExtensionPriority", emf.EString, 0, 1)
gmfgen.GenNavigator.newEAttribute("contentProviderClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEAttribute("labelProviderClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEAttribute("groupWrapperClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEReference("editorGen", gmfgen.GenEditorGenerator, 0, 1, false, null)
gmfgen.GenNavigator.editorGen.changeable = false
gmfgen.GenNavigator.newEReference("childReferences", gmfgen.GenNavigatorChildReference, 0, -1, true, null)
gmfgen.GenNavigator.newEOperation("getContentProviderQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEOperation("getLabelProviderQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEOperation("getGroupWrapperQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenNavigator.newEOperation("getChildReferencesFor", gmfgen.GenNavigatorChildReference, 0, -1)
gmfgen.GenNavigator.getChildReferencesFor.newEParameter("parent", gmfgen.GenCommonBase, 0, 1)

// reference GenEditorGenerator ---navigator--> GenNavigator
gmfgen.GenEditorGenerator.newEReference("navigator", gmfgen.GenNavigator, 1, 1, true, gmfgen.GenNavigator.editorGen)
