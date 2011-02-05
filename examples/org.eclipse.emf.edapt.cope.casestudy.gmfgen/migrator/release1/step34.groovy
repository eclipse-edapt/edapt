/* --------------------------------------------------------------------------------
 * revision 1.173
 * date: 2006-11-22 11:23:32 +0000;  author: atikhomirov;  lines: +4 -0;
 * [163214] attributes to distinguish Eclipse editor/view generation target
 * -------------------------------------------------------------------------------- */

// class GenEditorView
gmfgen.GenEditorView.newEAttribute("eclipseEditor", emf.EBoolean, 0, 1)
gmfgen.GenEditorView.eclipseEditor.defaultValueLiteral = "true"

// class OpenDiagramBehaviour
gmfgen.OpenDiagramBehaviour.newEAttribute("openAsEclipseEditor", emf.EBoolean, 0, 1)
gmfgen.OpenDiagramBehaviour.openAsEclipseEditor.defaultValueLiteral = "true"
