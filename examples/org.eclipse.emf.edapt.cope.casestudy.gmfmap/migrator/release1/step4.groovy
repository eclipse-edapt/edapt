/* --------------------------------------------------------------------------------
 * revision 1.47
 * date: 2006-10-03 18:11:04 +0000;  author: atikhomirov;  lines: +6 -0;
 * [119465] support for diagram partitioning
 * -------------------------------------------------------------------------------- */

// class MappingEntry
mappings.MappingEntry.newEReference("relatedDiagrams", mappings.CanvasMapping, 0, -1, false, null)
mappings.MappingEntry.relatedDiagrams.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.MappingEntry.relatedDiagrams.eAnnotations[0].newEStringToStringMapEntry("documentation", "Diagrams that may be associated with this diagram element. It's up to client application to define what this association means (e.g. open diagram)")
