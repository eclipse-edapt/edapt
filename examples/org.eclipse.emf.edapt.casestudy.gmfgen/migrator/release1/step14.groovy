/* --------------------------------------------------------------------------------
 * revision 1.153
 * date: 2006-10-02 16:54:54 +0000;  author: dstadnik;  lines: +15 -0;
 * #114200 add class names for RCP application in genmodel
 * -------------------------------------------------------------------------------- */

// class GenApplication
gmfgen.GenApplication.newEAttribute("applicationClassName", emf.EString, 0, 1)
gmfgen.GenApplication.newEAttribute("workbenchAdvisorClassName", emf.EString, 0, 1)
gmfgen.GenApplication.newEAttribute("applicationPackageName", emf.EString, 0, 1)
gmfgen.GenApplication.newEAttribute("perspectiveClassName", emf.EString, 0, 1)
gmfgen.GenApplication.newEAttribute("workbenchWindowAdvisorClassName", emf.EString, 0, 1)
gmfgen.GenApplication.newEAttribute("actionBarAdvisorClassName", emf.EString, 0, 1)

gmfgen.GenApplication.newEOperation("getWorkbenchAdvisorQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenApplication.newEOperation("getWorkbenchWindowAdvisorQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenApplication.newEOperation("getApplicationQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenApplication.newEOperation("getPerspectiveQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenApplication.newEOperation("getActionBarAdvisorQualifiedClassName", emf.EString, 0, 1)
