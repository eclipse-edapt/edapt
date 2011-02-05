/* --------------------------------------------------------------------------------
 * revision 1.3
 * date: 2006-10-31 14:56:46 +0000;  author: atikhomirov;  lines: +2 -1;
 * [134121] mgolubev - Tools stack definition in the model
 * [162456] mgolubev - Change GenericTool#toolClass type from Class to String
 * -------------------------------------------------------------------------------- */

// class ToolGroup
tooldef.ToolGroup.newEAttribute("stack", emf.EBoolean, 0, 1)

// class GenericTool
changeAttributeType(
		tooldef.GenericTool.toolClass,
		emf.EString)
