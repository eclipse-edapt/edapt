/* --------------------------------------------------------------------------------
 * revision 1.174
 * date: 2006-11-22 20:09:56 +0000;  author: atikhomirov;  lines: +19 -9;
 * [164021] allow to choose standard tools
 * -------------------------------------------------------------------------------- */

// class AbstractToolEntry
extractSuperClass(
	gmfgen.ToolEntry,
	[gmfgen.ToolEntry.qualifiedToolName, gmfgen.ToolEntry._properties, gmfgen.ToolEntry.default],
	gmfgen,
	"AbstractToolEntry",
	true,
	[gmfgen.EntryBase, gmfgen.ToolGroupItem]
)

// enum StandardEntryKind
gmfgen.newEEnum("StandardEntryKind")
gmfgen.StandardEntryKind.newEEnumLiteral("SELECT")
gmfgen.StandardEntryKind.SELECT.value = 0
gmfgen.StandardEntryKind.SELECT.instance = gmfgen.StandardEntryKind.SELECT
gmfgen.StandardEntryKind.SELECT.literal = "SELECT"
gmfgen.StandardEntryKind.newEEnumLiteral("MARQUEE")
gmfgen.StandardEntryKind.MARQUEE.value = 1
gmfgen.StandardEntryKind.MARQUEE.instance = gmfgen.StandardEntryKind.MARQUEE
gmfgen.StandardEntryKind.MARQUEE.literal = "MARQUEE"
gmfgen.StandardEntryKind.newEEnumLiteral("ZOOM")
gmfgen.StandardEntryKind.ZOOM.value = 2
gmfgen.StandardEntryKind.ZOOM.instance = gmfgen.StandardEntryKind.ZOOM
gmfgen.StandardEntryKind.ZOOM.literal = "ZOOM"

// class StandardEntry
gmfgen.newEClass("StandardEntry", [gmfgen.AbstractToolEntry], false)
gmfgen.StandardEntry.newEAttribute("kind", gmfgen.StandardEntryKind, 1, 1)
