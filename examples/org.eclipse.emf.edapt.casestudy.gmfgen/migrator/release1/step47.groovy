/* --------------------------------------------------------------------------------
 * revision 1.186
 * date: 2006-12-15 15:48:25 +0000;  author: dstadnik;  lines: +6 -4;
 * remove unused edit policy name; add custom primary drag edit policy name
 * -------------------------------------------------------------------------------- */

// class GenNode
gmfgen.GenNode.newEAttribute("primaryDragEditPolicyQualifiedClassName", emf.EString, 0, 1)
gmfgen.GenNode.primaryDragEditPolicyQualifiedClassName.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenNode.primaryDragEditPolicyQualifiedClassName.eAnnotations[0].newEStringToStringMapEntry("documentation", "Custom primary drag edit policy")

// class EditPartCandies
deleteFeature(gmfgen.EditPartCandies.externalNodeLabelHostLayoutEditPolicyClassName)
gmfgen.EditPartCandies.getExternalNodeLabelHostLayoutEditPolicyQualifiedClassName.delete()
