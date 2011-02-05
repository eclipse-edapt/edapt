/* --------------------------------------------------------------------------------
 * revision 1.53
 * date: 2007-05-03 09:59:11 +0000;  author: dstadnik;  lines: +7 -2;
 * refactor message format parser; introduce editor pattern
 * -------------------------------------------------------------------------------- */

// class FeatureLabelMapping
mappings.FeatureLabelMapping.newEAttribute("editorPattern", emf.EString, 0, 1)
mappings.FeatureLabelMapping.editorPattern.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.FeatureLabelMapping.editorPattern.eAnnotations[0].newEStringToStringMapEntry("documentation", "Pattern to produce text for inplace editor, depends on view method; if not specified then viewPattern should be used")

mappings.FeatureLabelMapping.editPattern.eAnnotations[0].details[0].value = mappings.FeatureLabelMapping.editPattern.eAnnotations[0].details[0].value.replace("produce text for inplace editor", "extract values from input text")
