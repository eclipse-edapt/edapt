/* --------------------------------------------------------------------------------
 * revision 1.158
 * date: 2006-10-06 12:15:31 +0000;  author: atikhomirov;  lines: +4 -6;
 * refactor common attribute (canonical ep name) into shared superclass, get ready to avoid generation of useless canonicalep classes
 * -------------------------------------------------------------------------------- */

// class GenContainerBase
gmfgen.GenContainerBase.newEOperation("needsCanonicalEditPolicy", emf.EBoolean, 0, 1)

pushFeature(gmfgen.EditPartCandies.canonicalEditPolicyClassName)
pullFeature([gmfgen.GenDiagram.canonicalEditPolicyClassName, gmfgen.GenChildContainer.canonicalEditPolicyClassName], gmfgen.GenContainerBase)

pushOperation(gmfgen.EditPartCandies.getCanonicalEditPolicyQualifiedClassName)
pullOperation([gmfgen.GenDiagram.getCanonicalEditPolicyQualifiedClassName, gmfgen.GenChildContainer.getCanonicalEditPolicyQualifiedClassName], gmfgen.GenContainerBase)
