/* --------------------------------------------------------------------------------
 * revision 1.24
 * date: 2006-08-24 15:38:47 +0000;  author: atikhomirov;  lines: +5 -0;
 * [154687] mgolubev - Support scalable polygons
 * -------------------------------------------------------------------------------- */

// class ScalablePolygon
gmfgraph.newEClass("ScalablePolygon", [gmfgraph.Polygon], false)
gmfgraph.ScalablePolygon.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.ScalablePolygon.eAnnotations[0].newEStringToStringMapEntry("documentation", "Marker interface to denote polygons with ability to autoscale to fit all available bounds. Separate class is needed instead of property in the Polygon class because of generalization PolygonDecoration extends Polygon")
