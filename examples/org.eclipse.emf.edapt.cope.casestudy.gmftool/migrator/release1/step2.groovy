/* --------------------------------------------------------------------------------
 * revision 1.4
 * date: 2006-10-31 15:04:40 +0000;  author: atikhomirov;  lines: +15 -3;
 * be specific about multiplicities
 * javadoc
 * -------------------------------------------------------------------------------- */

// class BundleImage
tooldef.BundleImage.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
tooldef.BundleImage.eAnnotations[0].newEStringToStringMapEntry("documentation", "Image bundled as part of distribution")

tooldef.BundleImage.path.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
tooldef.BundleImage.path.eAnnotations[0].newEStringToStringMapEntry("documentation", "Relative path to image")

tooldef.BundleImage.bundle.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
tooldef.BundleImage.bundle.eAnnotations[0].newEStringToStringMapEntry("documentation", "Empty value means image path is relative to generated bundle")

// class GenericTool
// critical
tooldef.GenericTool.toolClass.lowerBound = 1
