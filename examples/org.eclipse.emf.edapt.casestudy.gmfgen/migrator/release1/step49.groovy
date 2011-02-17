/* --------------------------------------------------------------------------------
 * revision 1.188
 * date: 2006-12-21 17:14:28 +0000;  author: dstadnik;  lines: +53 -0;
 * allow to specify ui contributions for rcp application in genmodel
 * -------------------------------------------------------------------------------- */

// class GenContributionItem
gmfgen.newEClass("GenContributionItem", [], true)
gmfgen.GenContributionItem.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenContributionItem.eAnnotations[0].newEStringToStringMapEntry("documentation", "Element of UI contribution")

// class GenActionFactoryContributionItem
gmfgen.newEClass("GenActionFactoryContributionItem", [gmfgen.GenContributionItem], false)
gmfgen.GenActionFactoryContributionItem.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenActionFactoryContributionItem.eAnnotations[0].newEStringToStringMapEntry("documentation", "Action from org.eclipse.ui.actions.ActionFactory")
gmfgen.GenActionFactoryContributionItem.newEAttribute("name", emf.EString, 1, 1)
gmfgen.GenActionFactoryContributionItem._name.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenActionFactoryContributionItem._name.eAnnotations[0].newEStringToStringMapEntry("documentation", "ActionFactory field name such as 'ABOUT'")

// class GenGroupMarker
gmfgen.newEClass("GenGroupMarker", [gmfgen.GenContributionItem], false)
gmfgen.GenGroupMarker.newEAttribute("groupName", emf.EString, 1, 1)

// class GenContributionManager
gmfgen.newEClass("GenContributionManager", [gmfgen.GenContributionItem], true)
gmfgen.GenContributionManager.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenContributionManager.eAnnotations[0].newEStringToStringMapEntry("documentation", "Group of UI contribution items")
gmfgen.GenContributionManager.newEAttribute("iD", emf.EString, 0, 1)
gmfgen.GenContributionManager.newEReference("items", gmfgen.GenContributionItem, 0, -1, true, null)

// class GenSharedContributionItem
gmfgen.newEClass("GenSharedContributionItem", [gmfgen.GenContributionItem], false)
gmfgen.GenSharedContributionItem.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenSharedContributionItem.eAnnotations[0].newEStringToStringMapEntry("documentation", "Reference to the shared contribution item")
gmfgen.GenSharedContributionItem.newEReference("actualItem", gmfgen.GenContributionItem, 1, 1, false, null)
gmfgen.GenSharedContributionItem.actualItem.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
gmfgen.GenSharedContributionItem.actualItem.eAnnotations[0].newEStringToStringMapEntry("ocl", "not actualItem.oclIsKindOf(gmfgen::GenSharedContributionItem)")
gmfgen.GenSharedContributionItem.actualItem.eAnnotations[0].newEStringToStringMapEntry("description", "Actual contribution item can't be a reference")

// class GenSeparator
gmfgen.newEClass("GenSeparator", [gmfgen.GenContributionItem], false)
gmfgen.GenSeparator.newEAttribute("groupName", emf.EString, 0, 1)

// class GenMenuManager
gmfgen.newEClass("GenMenuManager", [gmfgen.GenContributionManager], false)
gmfgen.GenMenuManager.newEAttribute("name", emf.EString, 0, 1)

// class GenToolBarManager
gmfgen.newEClass("GenToolBarManager", [gmfgen.GenContributionManager], false)

// class GenApplication
gmfgen.GenApplication.newEReference("mainToolBar", gmfgen.GenToolBarManager, 0, 1, true, null)
gmfgen.GenApplication.newEReference("sharedContributionItems", gmfgen.GenContributionItem, 0, -1, true, null)
gmfgen.GenApplication.newEReference("mainMenu", gmfgen.GenMenuManager, 0, 1, true, null)
