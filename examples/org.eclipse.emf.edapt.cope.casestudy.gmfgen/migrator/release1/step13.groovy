/* --------------------------------------------------------------------------------
 * revision 1.152
 * date: 2006-09-29 21:26:45 +0000;  author: atikhomirov;  lines: +16 -3;
 * [119465] support for diagram partitioning - started to reorganize the way we store behaviours in the genmodel - let them be reused. OpenDiagram as an example of new way to add behaviours.
 * -------------------------------------------------------------------------------- */

// class Behavior
extractSuperClass(
	gmfgen.CustomBehaviour,
	[],
	gmfgen,
	"Behaviour",
	false
)

gmfgen.Behaviour.'interface' = true
gmfgen.Behaviour.newEOperation("getEditPolicyQualifiedClassName", emf.EString, 0, 1)

// class SharedBehavior
gmfgen.newEClass("SharedBehaviour", [gmfgen.Behaviour], false)
gmfgen.SharedBehaviour.newEReference("delegate", gmfgen.Behaviour, 1, 1, false, null)

// class OpenDiagramBehavior
gmfgen.newEClass("OpenDiagramBehaviour", [gmfgen.Behaviour], false)
gmfgen.OpenDiagramBehaviour.newEAttribute("editPolicyClassName", emf.EString, 1, 1)


// class GenCommonBase
rename(gmfgen.GenCommonBase.customBehaviour, "behaviour")

generalizeReference(
	gmfgen.GenCommonBase.behaviour,
	gmfgen.Behaviour
)

newOppositeReference(gmfgen.GenCommonBase.behaviour, "subject", 0, 1, false)
