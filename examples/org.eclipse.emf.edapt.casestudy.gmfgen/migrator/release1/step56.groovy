/* --------------------------------------------------------------------------------
 * revision 1.195
 * date: 2007-01-03 21:21:08 +0000;  author: dstadnik;  lines: +3 -0;
 * make getIconX() available in model
 * -------------------------------------------------------------------------------- */

// class GenEditorView
gmfgen.GenEditorView.newEAttribute("iconPathX", emf.EString, 1, 1)
gmfgen.GenEditorView.iconPathX.changeable = false
gmfgen.GenEditorView.iconPathX.'volatile' = true
gmfgen.GenEditorView.iconPathX.'transient' = true
gmfgen.GenEditorView.iconPathX.derived = true
