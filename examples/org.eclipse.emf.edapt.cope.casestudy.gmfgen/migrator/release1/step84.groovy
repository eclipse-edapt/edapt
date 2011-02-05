/* --------------------------------------------------------------------------------
 * revision 1.223
 * date: 2007-05-04 11:55:25 +0000;  author: dstadnik;  lines: +6 -0;
 * use java 5 if printf parser is used
 * -------------------------------------------------------------------------------- */

// class GenEditorGenerator
gmfgen.GenEditorGenerator.newEOperation("requiresParser", emf.EBoolean, 0, 1)
gmfgen.GenEditorGenerator.requiresParser.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenEditorGenerator.requiresParser.eAnnotations[0].newEStringToStringMapEntry("documentation", "Returns true if parser for the specified method is used by diagram editor")
gmfgen.GenEditorGenerator.requiresParser.newEParameter("method", gmfgen.LabelTextAccessMethod, 0, 1)
