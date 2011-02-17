/* --------------------------------------------------------------------------------
 * revision 1.44
 * date: 2006-08-25 18:32:47 +0000;  author: atikhomirov;  lines: +8 -0;
 * [124826] mgolubev - Support nodes with border items
 * -------------------------------------------------------------------------------- */

// new OCL constraints
mappings.ChildReference.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.ChildReference.eAnnotations[4].newEStringToStringMapEntry("ocl", "let child:NodeMapping=(if ownedChild.oclIsUndefined() then referencedChild else ownedChild endif) in (((child.labelMappings->size() = 1) and child.labelMappings->forAll( soleLabel: LabelMapping | soleLabel.diagramLabel = child.diagramNode)) implies (child.diagramNode.affixedParentSide = gmfgraph::Direction::NSEW))")
mappings.ChildReference.eAnnotations[4].newEStringToStringMapEntry("description", "Side-affixed children can not be pure labels")

mappings.ChildReference.newEAnnotation("http://www.eclipse.org/gmf/2005/constraints")
mappings.ChildReference.eAnnotations[5].newEStringToStringMapEntry("ocl", "let child:NodeMapping=(if ownedChild.oclIsUndefined() then referencedChild else ownedChild endif) in ((not compartment.oclIsUndefined()) implies (child.diagramNode.affixedParentSide = gmfgraph::Direction::NSEW))")
mappings.ChildReference.eAnnotations[5].newEStringToStringMapEntry("description", "Side-affixed children can not be placed in compartments")
