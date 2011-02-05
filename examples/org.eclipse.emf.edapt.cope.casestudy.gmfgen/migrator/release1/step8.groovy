/* --------------------------------------------------------------------------------
 * revision 1.147
 * date: 2006-09-15 20:44:32 +0000;  author: atikhomirov;  lines: +48 -0;
 * Generate PropertySheet pluged into tabbed page framework of Eclipse instead of plug-in as runtime provider.
 * Eases pages being added/removed/replaced/tuned
 * -------------------------------------------------------------------------------- */

// class GenPropertySheet
gmfgen.newEClass("GenPropertySheet", [], false)
gmfgen.GenPropertySheet.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenPropertySheet.eAnnotations[0].newEStringToStringMapEntry("documentation", "Sheet consists of few categories (aka tabs)")
gmfgen.GenPropertySheet.newEAttribute("readOnly", emf.EBoolean, 0, 1)
gmfgen.GenPropertySheet.readOnly.defaultValueLiteral = "false"
gmfgen.GenPropertySheet.newEReference("editorGen", gmfgen.GenEditorGenerator, 1, 1, false, null)
gmfgen.GenPropertySheet.editorGen.changeable = false

// class GenPropertyTab
gmfgen.newEClass("GenPropertyTab", [], true)
gmfgen.GenPropertyTab.newEAttribute("iD", emf.EString, 1, 1)
gmfgen.GenPropertyTab.newEAttribute("label", emf.EString, 0, 1)
gmfgen.GenPropertyTab.newEReference("sheet", gmfgen.GenPropertySheet, 1, 1, false, null)
gmfgen.GenPropertyTab.sheet.changeable = false

// reference GenPropertySheet ---tabs--> GenPropertyTab
gmfgen.GenPropertySheet.newEReference("tabs", gmfgen.GenPropertyTab, 1, -1, true, gmfgen.GenPropertyTab.sheet)

// class GenStandardPropertyTab
gmfgen.newEClass("GenStandardPropertyTab", [gmfgen.GenPropertyTab], false)
gmfgen.GenStandardPropertyTab.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenStandardPropertyTab.eAnnotations[0].newEStringToStringMapEntry("documentation", "Standard property category plugs in predefined sets of properties (provided by runtime). Identifiers 'appearance', 'diagram' and 'advanced' are known at the moment")

// class GenCustomPropertyTab
gmfgen.newEClass("GenCustomPropertyTab", [gmfgen.GenPropertyTab], false)
gmfgen.GenCustomPropertyTab.newEOperation("getQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenCustomPropertyTab.newEAttribute("className", emf.EString, 1, 1)

// class GenPropertyTabFilter
gmfgen.newEClass("GenPropertyTabFilter", [], false)
gmfgen.GenPropertyTabFilter.'interface' = true
gmfgen.GenPropertyTabFilter.newEReference("tab", gmfgen.GenCustomPropertyTab, 1, 1, false, null)
gmfgen.GenPropertyTabFilter.tab.changeable = false

// reference GenCustomPropertyTab ---filter--> GenPropertyTabFilter
gmfgen.GenCustomPropertyTab.newEReference("filter", gmfgen.GenPropertyTabFilter, 0, 1, true, gmfgen.GenPropertyTabFilter.tab)

// class TypeTabFilter
gmfgen.newEClass("TypeTabFilter", [gmfgen.GenPropertyTabFilter], false)
gmfgen.TypeTabFilter.newEAttribute("types", emf.EString, 1, -1)
gmfgen.TypeTabFilter.types.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.TypeTabFilter.types.eAnnotations[0].newEStringToStringMapEntry("documentation", "Fully-qualified class names for selection to match")

// class CustomTabFilter
gmfgen.newEClass("CustomTabFilter", [gmfgen.GenPropertyTabFilter], false)
gmfgen.CustomTabFilter.newEAttribute("className", emf.EString, 1, 1)
gmfgen.CustomTabFilter.newEOperation("getQualifiedClassName", emf.EString, 0, 1)

// reference GenEditorGenerator ---propertySheet--> GenPropertySheet
gmfgen.GenEditorGenerator.newEReference("propertySheet", gmfgen.GenPropertySheet, 0, 1, true, gmfgen.GenPropertySheet.editorGen)
