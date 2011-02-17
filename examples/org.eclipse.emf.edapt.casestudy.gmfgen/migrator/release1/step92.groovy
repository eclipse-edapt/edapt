/* --------------------------------------------------------------------------------
 * revision 1.231
 * date: 2007-05-22 18:18:54 +0000;  author: ashatalin;  lines: +5 -0;
 * F5 triggering update of selected element added
 * -------------------------------------------------------------------------------- */

// class GenEditorView
gmfgen.GenEditorView.newEAttribute("contextID", emf.EString, 0, 1)

// class GenDiagramUpdater
gmfgen.GenDiagramUpdater.newEAttribute("updateCommandClassName", emf.EString, 0, 1)
gmfgen.GenDiagramUpdater.newEAttribute("updateCommandID", emf.EString, 0, 1)
gmfgen.GenDiagramUpdater.newEOperation("getUpdateCommandQualifiedClassName", emf.EString, 0, 1)
