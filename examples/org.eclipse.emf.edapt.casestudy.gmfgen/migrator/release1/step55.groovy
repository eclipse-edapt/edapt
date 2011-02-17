/* --------------------------------------------------------------------------------
 * revision 1.194
 * date: 2007-01-03 20:34:48 +0000;  author: dstadnik;  lines: +23 -0;
 * [134107] add entities for preference pages
 * -------------------------------------------------------------------------------- */

// enum StandardPreferencePages
gmfgen.newEEnum("StandardPreferencePages")
gmfgen.StandardPreferencePages.newEEnumLiteral("General")
gmfgen.StandardPreferencePages.General.value = 0
gmfgen.StandardPreferencePages.General.instance = gmfgen.StandardPreferencePages.General
gmfgen.StandardPreferencePages.General.literal = "General"
gmfgen.StandardPreferencePages.newEEnumLiteral("Appearance")
gmfgen.StandardPreferencePages.Appearance.value = 1
gmfgen.StandardPreferencePages.Appearance.instance = gmfgen.StandardPreferencePages.Appearance
gmfgen.StandardPreferencePages.Appearance.literal = "Appearance"
gmfgen.StandardPreferencePages.newEEnumLiteral("Connections")
gmfgen.StandardPreferencePages.Connections.value = 2
gmfgen.StandardPreferencePages.Connections.instance = gmfgen.StandardPreferencePages.Connections
gmfgen.StandardPreferencePages.Connections.literal = "Connections"
gmfgen.StandardPreferencePages.newEEnumLiteral("Printing")
gmfgen.StandardPreferencePages.Printing.value = 3
gmfgen.StandardPreferencePages.Printing.instance = gmfgen.StandardPreferencePages.Printing
gmfgen.StandardPreferencePages.Printing.literal = "Printing"
gmfgen.StandardPreferencePages.newEEnumLiteral("RulersAndGrid")
gmfgen.StandardPreferencePages.RulersAndGrid.value = 4
gmfgen.StandardPreferencePages.RulersAndGrid.instance = gmfgen.StandardPreferencePages.RulersAndGrid
gmfgen.StandardPreferencePages.RulersAndGrid.literal = "RulersAndGrid"
gmfgen.StandardPreferencePages.newEEnumLiteral("Pathmaps")
gmfgen.StandardPreferencePages.Pathmaps.value = 5
gmfgen.StandardPreferencePages.Pathmaps.instance = gmfgen.StandardPreferencePages.Pathmaps
gmfgen.StandardPreferencePages.Pathmaps.literal = "Pathmaps"

// class GenPreferencePage
gmfgen.newEClass("GenPreferencePage", [], true)
gmfgen.GenPreferencePage.newEAttribute("iD", emf.EString, 1, 1)
gmfgen.GenPreferencePage.newEAttribute("name", emf.EString, 1, 1)
gmfgen.GenPreferencePage.newEReference("children", gmfgen.GenPreferencePage, 0, -1, true, null)

// class GenCustomPreferencePage
gmfgen.newEClass("GenCustomPreferencePage", [gmfgen.GenPreferencePage], false)
gmfgen.GenCustomPreferencePage.newEAttribute("qualifiedClassName", emf.EString, 1, 1)

// class GenStandardPreferencePage
gmfgen.newEClass("GenStandardPreferencePage", [gmfgen.GenPreferencePage], false)
gmfgen.GenStandardPreferencePage.newEAttribute("kind", gmfgen.StandardPreferencePages, 1, 1)

// class GenDiagram
gmfgen.GenDiagram.newEReference("preferencePages", gmfgen.GenPreferencePage, 0, -1, true, null)
