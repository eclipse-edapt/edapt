/* --------------------------------------------------------------------------------
 * revision 1.187
 * date: 2006-12-18 18:20:51 +0000;  author: dstadnik;  lines: +21 -9;
 * rewrite application templates in xpand; simplify genmodel
 * -------------------------------------------------------------------------------- */

// class GenApplication
gmfgen.GenApplication.newEAttribute("supportFiles", emf.EBoolean, 0, 1)
gmfgen.GenApplication.supportFiles.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenApplication.supportFiles.eAnnotations[0].newEStringToStringMapEntry("documentation", "Option to create/save/open diagrams in/from java files")

gmfgen.GenApplication._iD.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenApplication._iD.eAnnotations[0].newEStringToStringMapEntry("documentation", "RCP Application ID for plugin.xml")

makeFeatureVolatile(gmfgen.GenApplication.workbenchAdvisorClassName)
makeFeatureVolatile(gmfgen.GenApplication.workbenchWindowAdvisorClassName)
makeFeatureVolatile(gmfgen.GenApplication.actionBarAdvisorClassName)
makeFeatureVolatile(gmfgen.GenApplication.perspectiveClassName)

rename(gmfgen.GenApplication.applicationPackageName, "packageName")
rename(gmfgen.GenApplication.applicationClassName, "className")
rename(gmfgen.GenApplication.getApplicationQualifiedClassName, "getQualifiedClassName")
