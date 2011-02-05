/* --------------------------------------------------------------------------------
 * revision 1.234
 * date: 2007-06-12 18:05:31 +0000;  author: dstadnik;  lines: +5 -0;
 * add method to find modeling assistant child nodes
 * -------------------------------------------------------------------------------- */

// class GenContainerBase
gmfgen.GenContainerBase.newEOperation("getAssistantNodes", gmfgen.GenNode, 0, -1)
gmfgen.GenContainerBase.getAssistantNodes.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenContainerBase.getAssistantNodes.eAnnotations[0].newEStringToStringMapEntry("documentation", "Returns child nodes that may be created in this container")
