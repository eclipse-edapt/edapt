/* --------------------------------------------------------------------------------
 * revision 1.140
 * date: 2006-07-07 11:58:57 +0000;  author: dstadnik;  lines: +14 -0;
 * add custom behaviour
 * -------------------------------------------------------------------------------- */

// class CustomBehaviour
gmfgen.newEClass("CustomBehaviour", [], false)
gmfgen.CustomBehaviour.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.CustomBehaviour.eAnnotations[0].newEStringToStringMapEntry("documentation", "Custom user behaviour")
gmfgen.CustomBehaviour.newEAttribute("key", emf.EString, 1, 1)
gmfgen.CustomBehaviour.key.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.CustomBehaviour.key.eAnnotations[0].newEStringToStringMapEntry("documentation", "Key used to register edit policy in host edit part")
gmfgen.CustomBehaviour.newEAttribute("editPolicyQualifiedClassName", emf.EString, 1, 1)

// reference GenCommonBase ---customBehaviour--> CustomBehaviour
gmfgen.GenCommonBase.newEReference("customBehaviour", gmfgen.CustomBehaviour, 0, -1, true, null)
