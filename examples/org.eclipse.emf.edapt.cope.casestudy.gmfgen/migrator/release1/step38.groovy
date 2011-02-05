/* --------------------------------------------------------------------------------
 * revision 1.177
 * date: 2006-11-28 18:39:23 +0000;  author: atikhomirov;  lines: +9 -3;
 * heading towards xpand templates - need some attributes to be available in the model
 * -------------------------------------------------------------------------------- */

// class ToolGroupItem
newOppositeReference(gmfgen.ToolGroup.entries, "group", 1, 1, false)

// class ToolGroup
newOppositeReference(gmfgen.Palette.groups, "palette", 1, 1, false)

// class Palette
gmfgen.Palette.newEOperation("definesStandardTools", emf.EBoolean, 0, 1)

