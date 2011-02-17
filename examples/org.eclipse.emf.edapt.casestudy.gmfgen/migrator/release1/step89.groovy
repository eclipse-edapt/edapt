/* --------------------------------------------------------------------------------
 * revision 1.228
 * date: 2007-05-18 13:28:04 +0000;  author: dstadnik;  lines: +6 -0;
 * [186339] Allow to specify custom notation styles in view factory
 * -------------------------------------------------------------------------------- */

// class GenCommonBase
gmfgen.GenCommonBase.newEReference("styles", genmodel.GenClass, 0, -1, false, null)
gmfgen.GenCommonBase.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
gmfgen.GenCommonBase.eAnnotations[1].newEStringToStringMapEntry("ocl", "styles->forAll(style|style.ecoreClass.eAllSuperTypes->including(style.ecoreClass)->one(ePackage.name = 'notation' and name = 'Style'))")
gmfgen.GenCommonBase.eAnnotations[1].newEStringToStringMapEntry("description", "Each style must be a notation::Style or sub-class")
