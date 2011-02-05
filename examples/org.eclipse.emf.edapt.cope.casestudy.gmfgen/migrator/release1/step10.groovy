/* --------------------------------------------------------------------------------
 * revision 1.149
 * date: 2006-09-18 19:08:54 +0000;  author: atikhomirov;  lines: +6 -0;
 * optionally generate label provider for caption of property sheet
 * -------------------------------------------------------------------------------- */

// class GenPropertySheet
gmfgen.GenPropertySheet.newEAttribute("needsCaption", emf.EBoolean, 0, 1)
gmfgen.GenPropertySheet.needsCaption.defaultValueLiteral = "true"
gmfgen.GenPropertySheet.newEAttribute("packageName", emf.EString, 0, 1)
gmfgen.GenPropertySheet.newEAttribute("labelProviderClassName", emf.EString, 0, 1)
gmfgen.GenPropertySheet.newEOperation("getLabelProviderQualifiedClassName", emf.EString, 0, 1)
