/* --------------------------------------------------------------------------------
 * revision 1.247
 * date: 2008-05-19 11:02:56 +0000;  author: dstadnik;  lines: +15 -0;
 * [232761] support new diagram prefs
 * -------------------------------------------------------------------------------- */

// enum LineStyle
gmfgen.newEEnum("LineStyle")
gmfgen.LineStyle.newEEnumLiteral("SOLID")
gmfgen.LineStyle.SOLID.value = 0
gmfgen.LineStyle.SOLID.instance = gmfgen.LineStyle.SOLID
gmfgen.LineStyle.SOLID.literal = "SOLID"
gmfgen.LineStyle.newEEnumLiteral("DASH")
gmfgen.LineStyle.DASH.value = 1
gmfgen.LineStyle.DASH.instance = gmfgen.LineStyle.DASH
gmfgen.LineStyle.DASH.literal = "DASH"
gmfgen.LineStyle.newEEnumLiteral("DOT")
gmfgen.LineStyle.DOT.value = 2
gmfgen.LineStyle.DOT.instance = gmfgen.LineStyle.DOT
gmfgen.LineStyle.DOT.literal = "DOT"
gmfgen.LineStyle.newEEnumLiteral("DASHDOT")
gmfgen.LineStyle.DASHDOT.value = 3
gmfgen.LineStyle.DASHDOT.instance = gmfgen.LineStyle.DASHDOT
gmfgen.LineStyle.DASHDOT.literal = "DASHDOT"
gmfgen.LineStyle.newEEnumLiteral("DASHDOTDOT")
gmfgen.LineStyle.DASHDOTDOT.value = 4
gmfgen.LineStyle.DASHDOTDOT.instance = gmfgen.LineStyle.DASHDOTDOT
gmfgen.LineStyle.DASHDOTDOT.literal = "DASHDOTDOT"
gmfgen.LineStyle.newEEnumLiteral("CUSTOM")
gmfgen.LineStyle.CUSTOM.value = 5
gmfgen.LineStyle.CUSTOM.instance = gmfgen.LineStyle.CUSTOM
gmfgen.LineStyle.CUSTOM.literal = "CUSTOM"

// class GenDiagramPreferences
gmfgen.GenDiagramPreferences.newEAttribute("gridInFront", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.gridInFront.defaultValueLiteral = "true"
gmfgen.GenDiagramPreferences.newEReference("gridLineColor", gmfgen.GenColor, 0, 1, true, null)
gmfgen.GenDiagramPreferences.newEAttribute("snapToGeometry", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.snapToGeometry.defaultValueLiteral = "false"
gmfgen.GenDiagramPreferences.newEAttribute("gridLineStyle", gmfgen.LineStyle, 0, 1)
