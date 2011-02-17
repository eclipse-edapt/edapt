/* --------------------------------------------------------------------------------
 * revision 1.226
 * date: 2007-05-11 16:01:30 +0000;  author: ashatalin;  lines: +6 -2;
 * GenDiagramUpdater created in .gmfgen model
 * -------------------------------------------------------------------------------- */

// class GenDiagramUpdater
rename(gmfgen.Updater, "GenDiagramUpdater")
gmfgen.GenDiagramUpdater.'abstract' = false
gmfgen.GenDiagramUpdater.'interface' = false

replaceInheritanceByDelegation(gmfgen.GenDiagram, gmfgen.GenDiagramUpdater, "diagramUpdater")
collectFeature(gmfgen.GenDiagram.diagramUpdater, gmfgen.GenEditorGenerator.diagram)

newOppositeReference(gmfgen.GenEditorGenerator.diagramUpdater, "editorGen", 0, 1, false)
