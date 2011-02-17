/* --------------------------------------------------------------------------------
 * revision 1.220
 * date: 2007-05-01 13:28:04 +0000;  author: ashatalin;  lines: +7 -2;
 * [181167] - Separate sycnhronization logic from CanonicalEditPolicy / DiagramContentsInitializer
 * -------------------------------------------------------------------------------- */

// class Updater
extractSuperClass(
	gmfgen.GenDiagram,
	[],
	gmfgen,
	"Updater"
)

gmfgen.Updater.'interface' = true
gmfgen.Updater.newEOperation("getDiagramUpdaterQualifiedClassName", emf.EString, 0, 1)
gmfgen.Updater.newEAttribute("diagramUpdaterClassName", emf.EString, 0, 1)

// class GenExpressionProviderBase
gmfgen.GenExpressionProviderBase._container.changeable = false
