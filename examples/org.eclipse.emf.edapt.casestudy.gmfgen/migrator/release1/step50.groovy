/* --------------------------------------------------------------------------------
 * revision 1.189
 * date: 2006-12-27 12:43:26 +0000;  author: dstadnik;  lines: +125 -0;
 * [134107] Provide preferences for the generated diagram editors
 * -------------------------------------------------------------------------------- */

// enum DiagramColors
gmfgen.newEEnum("DiagramColors")
gmfgen.DiagramColors.newEEnumLiteral("buttonLightest")
gmfgen.DiagramColors.buttonLightest.value = 0
gmfgen.DiagramColors.buttonLightest.instance = gmfgen.DiagramColors.buttonLightest
gmfgen.DiagramColors.buttonLightest.literal = "buttonLightest"
gmfgen.DiagramColors.newEEnumLiteral("button")
gmfgen.DiagramColors.button.value = 1
gmfgen.DiagramColors.button.instance = gmfgen.DiagramColors.button
gmfgen.DiagramColors.button.literal = "button"
gmfgen.DiagramColors.newEEnumLiteral("buttonDarker")
gmfgen.DiagramColors.buttonDarker.value = 2
gmfgen.DiagramColors.buttonDarker.instance = gmfgen.DiagramColors.buttonDarker
gmfgen.DiagramColors.buttonDarker.literal = "buttonDarker"
gmfgen.DiagramColors.newEEnumLiteral("buttonDarkest")
gmfgen.DiagramColors.buttonDarkest.value = 3
gmfgen.DiagramColors.buttonDarkest.instance = gmfgen.DiagramColors.buttonDarkest
gmfgen.DiagramColors.buttonDarkest.literal = "buttonDarkest"
gmfgen.DiagramColors.newEEnumLiteral("listBackground")
gmfgen.DiagramColors.listBackground.value = 4
gmfgen.DiagramColors.listBackground.instance = gmfgen.DiagramColors.listBackground
gmfgen.DiagramColors.listBackground.literal = "listBackground"
gmfgen.DiagramColors.newEEnumLiteral("listForeground")
gmfgen.DiagramColors.listForeground.value = 5
gmfgen.DiagramColors.listForeground.instance = gmfgen.DiagramColors.listForeground
gmfgen.DiagramColors.listForeground.literal = "listForeground"
gmfgen.DiagramColors.newEEnumLiteral("menuBackground")
gmfgen.DiagramColors.menuBackground.value = 6
gmfgen.DiagramColors.menuBackground.instance = gmfgen.DiagramColors.menuBackground
gmfgen.DiagramColors.menuBackground.literal = "menuBackground"
gmfgen.DiagramColors.newEEnumLiteral("menuForeground")
gmfgen.DiagramColors.menuForeground.value = 7
gmfgen.DiagramColors.menuForeground.instance = gmfgen.DiagramColors.menuForeground
gmfgen.DiagramColors.menuForeground.literal = "menuForeground"
gmfgen.DiagramColors.newEEnumLiteral("menuBackgroundSelected")
gmfgen.DiagramColors.menuBackgroundSelected.value = 8
gmfgen.DiagramColors.menuBackgroundSelected.instance = gmfgen.DiagramColors.menuBackgroundSelected
gmfgen.DiagramColors.menuBackgroundSelected.literal = "menuBackgroundSelected"
gmfgen.DiagramColors.newEEnumLiteral("menuForegroundSelected")
gmfgen.DiagramColors.menuForegroundSelected.value = 9
gmfgen.DiagramColors.menuForegroundSelected.instance = gmfgen.DiagramColors.menuForegroundSelected
gmfgen.DiagramColors.menuForegroundSelected.literal = "menuForegroundSelected"
gmfgen.DiagramColors.newEEnumLiteral("titleBackground")
gmfgen.DiagramColors.titleBackground.value = 10
gmfgen.DiagramColors.titleBackground.instance = gmfgen.DiagramColors.titleBackground
gmfgen.DiagramColors.titleBackground.literal = "titleBackground"
gmfgen.DiagramColors.newEEnumLiteral("titleGradient")
gmfgen.DiagramColors.titleGradient.value = 11
gmfgen.DiagramColors.titleGradient.instance = gmfgen.DiagramColors.titleGradient
gmfgen.DiagramColors.titleGradient.literal = "titleGradient"
gmfgen.DiagramColors.newEEnumLiteral("titleForeground")
gmfgen.DiagramColors.titleForeground.value = 12
gmfgen.DiagramColors.titleForeground.instance = gmfgen.DiagramColors.titleForeground
gmfgen.DiagramColors.titleForeground.literal = "titleForeground"
gmfgen.DiagramColors.newEEnumLiteral("titleInactiveForeground")
gmfgen.DiagramColors.titleInactiveForeground.value = 13
gmfgen.DiagramColors.titleInactiveForeground.instance = gmfgen.DiagramColors.titleInactiveForeground
gmfgen.DiagramColors.titleInactiveForeground.literal = "titleInactiveForeground"
gmfgen.DiagramColors.newEEnumLiteral("titleInactiveBackground")
gmfgen.DiagramColors.titleInactiveBackground.value = 14
gmfgen.DiagramColors.titleInactiveBackground.instance = gmfgen.DiagramColors.titleInactiveBackground
gmfgen.DiagramColors.titleInactiveBackground.literal = "titleInactiveBackground"
gmfgen.DiagramColors.newEEnumLiteral("titleInactiveGradient")
gmfgen.DiagramColors.titleInactiveGradient.value = 15
gmfgen.DiagramColors.titleInactiveGradient.instance = gmfgen.DiagramColors.titleInactiveGradient
gmfgen.DiagramColors.titleInactiveGradient.literal = "titleInactiveGradient"
gmfgen.DiagramColors.newEEnumLiteral("tooltipForeground")
gmfgen.DiagramColors.tooltipForeground.value = 16
gmfgen.DiagramColors.tooltipForeground.instance = gmfgen.DiagramColors.tooltipForeground
gmfgen.DiagramColors.tooltipForeground.literal = "tooltipForeground"
gmfgen.DiagramColors.newEEnumLiteral("tooltipBackground")
gmfgen.DiagramColors.tooltipBackground.value = 17
gmfgen.DiagramColors.tooltipBackground.instance = gmfgen.DiagramColors.tooltipBackground
gmfgen.DiagramColors.tooltipBackground.literal = "tooltipBackground"
gmfgen.DiagramColors.newEEnumLiteral("white")
gmfgen.DiagramColors.white.value = 18
gmfgen.DiagramColors.white.instance = gmfgen.DiagramColors.white
gmfgen.DiagramColors.white.literal = "white"
gmfgen.DiagramColors.newEEnumLiteral("lightGray")
gmfgen.DiagramColors.lightGray.value = 19
gmfgen.DiagramColors.lightGray.instance = gmfgen.DiagramColors.lightGray
gmfgen.DiagramColors.lightGray.literal = "lightGray"
gmfgen.DiagramColors.newEEnumLiteral("gray")
gmfgen.DiagramColors.gray.value = 20
gmfgen.DiagramColors.gray.instance = gmfgen.DiagramColors.gray
gmfgen.DiagramColors.gray.literal = "gray"
gmfgen.DiagramColors.newEEnumLiteral("darkGray")
gmfgen.DiagramColors.darkGray.value = 21
gmfgen.DiagramColors.darkGray.instance = gmfgen.DiagramColors.darkGray
gmfgen.DiagramColors.darkGray.literal = "darkGray"
gmfgen.DiagramColors.newEEnumLiteral("black")
gmfgen.DiagramColors.black.value = 22
gmfgen.DiagramColors.black.instance = gmfgen.DiagramColors.black
gmfgen.DiagramColors.black.literal = "black"
gmfgen.DiagramColors.newEEnumLiteral("red")
gmfgen.DiagramColors.red.value = 23
gmfgen.DiagramColors.red.instance = gmfgen.DiagramColors.red
gmfgen.DiagramColors.red.literal = "red"
gmfgen.DiagramColors.newEEnumLiteral("orange")
gmfgen.DiagramColors.orange.value = 24
gmfgen.DiagramColors.orange.instance = gmfgen.DiagramColors.orange
gmfgen.DiagramColors.orange.literal = "orange"
gmfgen.DiagramColors.newEEnumLiteral("yellow")
gmfgen.DiagramColors.yellow.value = 25
gmfgen.DiagramColors.yellow.instance = gmfgen.DiagramColors.yellow
gmfgen.DiagramColors.yellow.literal = "yellow"
gmfgen.DiagramColors.newEEnumLiteral("green")
gmfgen.DiagramColors.green.value = 26
gmfgen.DiagramColors.green.instance = gmfgen.DiagramColors.green
gmfgen.DiagramColors.green.literal = "green"
gmfgen.DiagramColors.newEEnumLiteral("lightGreen")
gmfgen.DiagramColors.lightGreen.value = 27
gmfgen.DiagramColors.lightGreen.instance = gmfgen.DiagramColors.lightGreen
gmfgen.DiagramColors.lightGreen.literal = "lightGreen"
gmfgen.DiagramColors.newEEnumLiteral("darkGreen")
gmfgen.DiagramColors.darkGreen.value = 28
gmfgen.DiagramColors.darkGreen.instance = gmfgen.DiagramColors.darkGreen
gmfgen.DiagramColors.darkGreen.literal = "darkGreen"
gmfgen.DiagramColors.newEEnumLiteral("cyan")
gmfgen.DiagramColors.cyan.value = 29
gmfgen.DiagramColors.cyan.instance = gmfgen.DiagramColors.cyan
gmfgen.DiagramColors.cyan.literal = "cyan"
gmfgen.DiagramColors.newEEnumLiteral("lightBlue")
gmfgen.DiagramColors.lightBlue.value = 30
gmfgen.DiagramColors.lightBlue.instance = gmfgen.DiagramColors.lightBlue
gmfgen.DiagramColors.lightBlue.literal = "lightBlue"
gmfgen.DiagramColors.newEEnumLiteral("blue")
gmfgen.DiagramColors.blue.value = 31
gmfgen.DiagramColors.blue.instance = gmfgen.DiagramColors.blue
gmfgen.DiagramColors.blue.literal = "blue"
gmfgen.DiagramColors.newEEnumLiteral("darkBlue")
gmfgen.DiagramColors.darkBlue.value = 32
gmfgen.DiagramColors.darkBlue.instance = gmfgen.DiagramColors.darkBlue
gmfgen.DiagramColors.darkBlue.literal = "darkBlue"
gmfgen.DiagramColors.newEEnumLiteral("diagramGreen")
gmfgen.DiagramColors.diagramGreen.value = 33
gmfgen.DiagramColors.diagramGreen.instance = gmfgen.DiagramColors.diagramGreen
gmfgen.DiagramColors.diagramGreen.literal = "diagramGreen"
gmfgen.DiagramColors.newEEnumLiteral("diagramLightRed")
gmfgen.DiagramColors.diagramLightRed.value = 34
gmfgen.DiagramColors.diagramLightRed.instance = gmfgen.DiagramColors.diagramLightRed
gmfgen.DiagramColors.diagramLightRed.literal = "diagramLightRed"
gmfgen.DiagramColors.newEEnumLiteral("diagramRed")
gmfgen.DiagramColors.diagramRed.value = 35
gmfgen.DiagramColors.diagramRed.instance = gmfgen.DiagramColors.diagramRed
gmfgen.DiagramColors.diagramRed.literal = "diagramRed"
gmfgen.DiagramColors.newEEnumLiteral("diagramLightBlue")
gmfgen.DiagramColors.diagramLightBlue.value = 36
gmfgen.DiagramColors.diagramLightBlue.instance = gmfgen.DiagramColors.diagramLightBlue
gmfgen.DiagramColors.diagramLightBlue.literal = "diagramLightBlue"
gmfgen.DiagramColors.newEEnumLiteral("diagramBlue")
gmfgen.DiagramColors.diagramBlue.value = 37
gmfgen.DiagramColors.diagramBlue.instance = gmfgen.DiagramColors.diagramBlue
gmfgen.DiagramColors.diagramBlue.literal = "diagramBlue"
gmfgen.DiagramColors.newEEnumLiteral("diagramLightGray")
gmfgen.DiagramColors.diagramLightGray.value = 38
gmfgen.DiagramColors.diagramLightGray.instance = gmfgen.DiagramColors.diagramLightGray
gmfgen.DiagramColors.diagramLightGray.literal = "diagramLightGray"
gmfgen.DiagramColors.newEEnumLiteral("diagramGray")
gmfgen.DiagramColors.diagramGray.value = 39
gmfgen.DiagramColors.diagramGray.instance = gmfgen.DiagramColors.diagramGray
gmfgen.DiagramColors.diagramGray.literal = "diagramGray"
gmfgen.DiagramColors.newEEnumLiteral("diagramDarkGray")
gmfgen.DiagramColors.diagramDarkGray.value = 40
gmfgen.DiagramColors.diagramDarkGray.instance = gmfgen.DiagramColors.diagramDarkGray
gmfgen.DiagramColors.diagramDarkGray.literal = "diagramDarkGray"
gmfgen.DiagramColors.newEEnumLiteral("diagramLightYellow")
gmfgen.DiagramColors.diagramLightYellow.value = 41
gmfgen.DiagramColors.diagramLightYellow.instance = gmfgen.DiagramColors.diagramLightYellow
gmfgen.DiagramColors.diagramLightYellow.literal = "diagramLightYellow"
gmfgen.DiagramColors.newEEnumLiteral("diagramDarkYellow")
gmfgen.DiagramColors.diagramDarkYellow.value = 42
gmfgen.DiagramColors.diagramDarkYellow.instance = gmfgen.DiagramColors.diagramDarkYellow
gmfgen.DiagramColors.diagramDarkYellow.literal = "diagramDarkYellow"
gmfgen.DiagramColors.newEEnumLiteral("diagramLightGoldYellow")
gmfgen.DiagramColors.diagramLightGoldYellow.value = 43
gmfgen.DiagramColors.diagramLightGoldYellow.instance = gmfgen.DiagramColors.diagramLightGoldYellow
gmfgen.DiagramColors.diagramLightGoldYellow.literal = "diagramLightGoldYellow"
gmfgen.DiagramColors.newEEnumLiteral("diagramBurgundyRed")
gmfgen.DiagramColors.diagramBurgundyRed.value = 44
gmfgen.DiagramColors.diagramBurgundyRed.instance = gmfgen.DiagramColors.diagramBurgundyRed
gmfgen.DiagramColors.diagramBurgundyRed.literal = "diagramBurgundyRed"

// enum JFaceFont
gmfgen.newEEnum("JFaceFont")
gmfgen.JFaceFont.newEEnumLiteral("Default")
gmfgen.JFaceFont.Default.value = 0
gmfgen.JFaceFont.Default.instance = gmfgen.JFaceFont.Default
gmfgen.JFaceFont.Default.literal = "Default"
gmfgen.JFaceFont.newEEnumLiteral("Text")
gmfgen.JFaceFont.Text.value = 1
gmfgen.JFaceFont.Text.instance = gmfgen.JFaceFont.Text
gmfgen.JFaceFont.Text.literal = "Text"
gmfgen.JFaceFont.newEEnumLiteral("Banner")
gmfgen.JFaceFont.Banner.value = 2
gmfgen.JFaceFont.Banner.instance = gmfgen.JFaceFont.Banner
gmfgen.JFaceFont.Banner.literal = "Banner"
gmfgen.JFaceFont.newEEnumLiteral("Dialog")
gmfgen.JFaceFont.Dialog.value = 3
gmfgen.JFaceFont.Dialog.instance = gmfgen.JFaceFont.Dialog
gmfgen.JFaceFont.Dialog.literal = "Dialog"
gmfgen.JFaceFont.newEEnumLiteral("Header")
gmfgen.JFaceFont.Header.value = 4
gmfgen.JFaceFont.Header.instance = gmfgen.JFaceFont.Header
gmfgen.JFaceFont.Header.literal = "Header"

// enum RulerUnits
gmfgen.newEEnum("RulerUnits")
gmfgen.RulerUnits.newEEnumLiteral("Inches")
gmfgen.RulerUnits.Inches.value = 0
gmfgen.RulerUnits.Inches.instance = gmfgen.RulerUnits.Inches
gmfgen.RulerUnits.Inches.literal = "Inches"
gmfgen.RulerUnits.newEEnumLiteral("Centimeters")
gmfgen.RulerUnits.Centimeters.value = 1
gmfgen.RulerUnits.Centimeters.instance = gmfgen.RulerUnits.Centimeters
gmfgen.RulerUnits.Centimeters.literal = "Centimeters"
gmfgen.RulerUnits.newEEnumLiteral("Pixels")
gmfgen.RulerUnits.Pixels.value = 2
gmfgen.RulerUnits.Pixels.instance = gmfgen.RulerUnits.Pixels
gmfgen.RulerUnits.Pixels.literal = "Pixels"

// enum Routing
gmfgen.newEEnum("Routing")
gmfgen.Routing.newEEnumLiteral("Manual")
gmfgen.Routing.Manual.value = 0
gmfgen.Routing.Manual.instance = gmfgen.Routing.Manual
gmfgen.Routing.Manual.literal = "Manual"
gmfgen.Routing.newEEnumLiteral("Rectilinear")
gmfgen.Routing.Rectilinear.value = 1
gmfgen.Routing.Rectilinear.instance = gmfgen.Routing.Rectilinear
gmfgen.Routing.Rectilinear.literal = "Rectilinear"
gmfgen.Routing.newEEnumLiteral("Tree")
gmfgen.Routing.Tree.value = 2
gmfgen.Routing.Tree.instance = gmfgen.Routing.Tree
gmfgen.Routing.Tree.literal = "Tree"

// enum FontStyle
gmfgen.newEEnum("FontStyle")
gmfgen.FontStyle.newEEnumLiteral("NORMAL")
gmfgen.FontStyle.NORMAL.value = 0
gmfgen.FontStyle.NORMAL.instance = gmfgen.FontStyle.NORMAL
gmfgen.FontStyle.NORMAL.literal = "NORMAL"
gmfgen.FontStyle.newEEnumLiteral("BOLD")
gmfgen.FontStyle.BOLD.value = 1
gmfgen.FontStyle.BOLD.instance = gmfgen.FontStyle.BOLD
gmfgen.FontStyle.BOLD.literal = "BOLD"
gmfgen.FontStyle.newEEnumLiteral("ITALIC")
gmfgen.FontStyle.ITALIC.value = 2
gmfgen.FontStyle.ITALIC.instance = gmfgen.FontStyle.ITALIC
gmfgen.FontStyle.ITALIC.literal = "ITALIC"

// class GenFont
gmfgen.newEClass("GenFont", [], false)
gmfgen.GenFont.'interface' = true

// class GenColor
gmfgen.newEClass("GenColor", [], false)
gmfgen.GenColor.'interface' = true

// class GenStandardFont
gmfgen.newEClass("GenStandardFont", [gmfgen.GenFont], false)
gmfgen.GenStandardFont.newEAttribute("name", gmfgen.JFaceFont, 0, 1)

// class GenCustomFont
gmfgen.newEClass("GenCustomFont", [gmfgen.GenFont], false)
gmfgen.GenCustomFont.newEAttribute("name", emf.EString, 0, 1)
gmfgen.GenCustomFont.newEAttribute("height", emf.EInt, 0, 1)
gmfgen.GenCustomFont.newEAttribute("style", gmfgen.FontStyle, 0, 1)

// class GenConstantColor
gmfgen.newEClass("GenConstantColor", [gmfgen.GenColor], false)
gmfgen.GenConstantColor.newEAttribute("name", gmfgen.DiagramColors, 0, 1)

// class GenRGBColor
gmfgen.newEClass("GenRGBColor", [gmfgen.GenColor], false)
gmfgen.GenRGBColor.newEAttribute("red", emf.EInt, 1, 1)
gmfgen.GenRGBColor.newEAttribute("green", emf.EInt, 1, 1)
gmfgen.GenRGBColor.newEAttribute("blue", emf.EInt, 1, 1)

// class GenDiagramPreferences
gmfgen.newEClass("GenDiagramPreferences", [], false)
gmfgen.GenDiagramPreferences.newEAttribute("lineStyle", gmfgen.Routing, 0, 1)
gmfgen.GenDiagramPreferences.newEReference("defaultFont", gmfgen.GenFont, 0, 1, true, null)
gmfgen.GenDiagramPreferences.newEReference("fontColor", gmfgen.GenColor, 0, 1, true, null)
gmfgen.GenDiagramPreferences.newEReference("fillColor", gmfgen.GenColor, 0, 1, true, null)
gmfgen.GenDiagramPreferences.newEReference("lineColor", gmfgen.GenColor, 0, 1, true, null)
gmfgen.GenDiagramPreferences.newEReference("noteFillColor", gmfgen.GenColor, 0, 1, true, null)
gmfgen.GenDiagramPreferences.newEReference("noteLineColor", gmfgen.GenColor, 0, 1, true, null)
gmfgen.GenDiagramPreferences.newEAttribute("showConnectionHandles", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.showConnectionHandles.defaultValueLiteral = "true"
gmfgen.GenDiagramPreferences.newEAttribute("showPopupBars", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.showPopupBars.defaultValueLiteral = "true"
gmfgen.GenDiagramPreferences.newEAttribute("promptOnDelFromModel", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.newEAttribute("promptOnDelFromDiagram", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.newEAttribute("enableAnimatedLayout", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.enableAnimatedLayout.defaultValueLiteral = "true"
gmfgen.GenDiagramPreferences.newEAttribute("enableAnimatedZoom", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.enableAnimatedZoom.defaultValueLiteral = "true"
gmfgen.GenDiagramPreferences.newEAttribute("enableAntiAlias", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.enableAntiAlias.defaultValueLiteral = "true"
gmfgen.GenDiagramPreferences.newEAttribute("showGrid", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.showGrid.defaultValueLiteral = "false"
gmfgen.GenDiagramPreferences.newEAttribute("showRulers", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.showRulers.defaultValueLiteral = "false"
gmfgen.GenDiagramPreferences.newEAttribute("snapToGrid", emf.EBoolean, 0, 1)
gmfgen.GenDiagramPreferences.snapToGrid.defaultValueLiteral = "true"
gmfgen.GenDiagramPreferences.newEAttribute("rulerUnits", gmfgen.RulerUnits, 0, 1)
gmfgen.GenDiagramPreferences.newEAttribute("gridSpacing", emf.EDouble, 0, 1)

// class GenDiagram
gmfgen.GenDiagram.newEReference("preferences", gmfgen.GenDiagramPreferences, 0, 1, true, null)
