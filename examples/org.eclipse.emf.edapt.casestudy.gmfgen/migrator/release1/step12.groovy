/* --------------------------------------------------------------------------------
 * revision 1.151
 * date: 2006-09-27 10:04:42 +0000;  author: dstadnik;  lines: +10 -0;
 * #114200 Provide an option to generate RCP application
 * -------------------------------------------------------------------------------- */

// class GenApplication
gmfgen.newEClass("GenApplication", [], false)
gmfgen.GenApplication.newEReference("editorGen", gmfgen.GenEditorGenerator, 1, 1, false, null)
gmfgen.GenApplication.editorGen.changeable = false

// class GenEditorGenerator
gmfgen.GenEditorGenerator.newEReference("application", gmfgen.GenApplication, 0, 1, true, gmfgen.GenApplication.editorGen)
gmfgen.GenEditorGenerator.application.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenEditorGenerator.application.eAnnotations[0].newEStringToStringMapEntry("documentation", "If application is defined within the model then generator should target RCP")

