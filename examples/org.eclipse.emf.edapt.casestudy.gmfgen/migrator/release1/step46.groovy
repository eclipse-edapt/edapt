/* --------------------------------------------------------------------------------
 * revision 1.185
 * date: 2006-12-12 18:10:58 +0000;  author: dstadnik;  lines: +14 -10;
 * convert operations in resize constraints to attributes so they may be used in xpand templates
 * -------------------------------------------------------------------------------- */

// class ResizeConstraints
operationToVolatile(gmfgen.ResizeConstraints.getResizeHandleNames)
gmfgen.ResizeConstraints.resizeHandleNames.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.ResizeConstraints.resizeHandleNames.eAnnotations[0].newEStringToStringMapEntry("documentation", "Convenient method to get {@link org.eclipse.draw2d.PositionConstants} names from resizeHandle attribute")

operationToVolatile(gmfgen.ResizeConstraints.getNonResizeHandleNames)
gmfgen.ResizeConstraints.nonResizeHandleNames.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.ResizeConstraints.nonResizeHandleNames.eAnnotations[0].newEStringToStringMapEntry("documentation", "Same as {@link #getResizeHandleNames()}, for nonResizeHandle attribute")
