/* --------------------------------------------------------------------------------
 * revision 1.33
 * date: 2008-05-21 16:42:10 +0000;  author: atikhomirov;  lines: +34 -0;
 * [139126] respect implementation bundle of custom borders and layouts
 * -------------------------------------------------------------------------------- */

// class BorderRef
gmfgraph.newEClass("BorderRef", [gmfgraph.Border], false)
gmfgraph.BorderRef.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.BorderRef.eAnnotations[0].newEStringToStringMapEntry("documentation", "Border reuse mechanism")
gmfgraph.BorderRef.newEReference("actual", gmfgraph.Border, 1, 1, false, null)
gmfgraph.BorderRef.actual.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.BorderRef.actual.eAnnotations[0].newEStringToStringMapEntry("documentation", "constraint: actual should not be another BorderRef")

// class LayoutRef
gmfgraph.newEClass("LayoutRef", [gmfgraph.Layout], false)
gmfgraph.LayoutRef.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.LayoutRef.eAnnotations[0].newEStringToStringMapEntry("documentation", "Layout reuse mechanism")
gmfgraph.LayoutRef.newEReference("actual", gmfgraph.Layout, 1, 1, false, null)
gmfgraph.LayoutRef.actual.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.LayoutRef.actual.eAnnotations[0].newEStringToStringMapEntry("documentation", "constraint: actual should not be another LayoutRef")

// class FigureGallery
gmfgraph.FigureGallery.newEReference("layouts", gmfgraph.Layout, 0, -1, true, null)
gmfgraph.FigureGallery.layouts.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.FigureGallery.layouts.eAnnotations[0].newEStringToStringMapEntry("documentation", "Layouts for reuse")
gmfgraph.FigureGallery.newEReference("borders", gmfgraph.Border, 0, -1, true, null)
gmfgraph.FigureGallery.borders.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.FigureGallery.borders.eAnnotations[0].newEStringToStringMapEntry("documentation", "Borders for reuse")
