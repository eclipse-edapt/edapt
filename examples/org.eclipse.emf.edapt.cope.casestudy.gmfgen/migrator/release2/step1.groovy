/* --------------------------------------------------------------------------------
 * revision 1.235
 * date: 2007-09-07 18:16:50 +0000;  author: dstadnik;  lines: +4 -1;
 * regenerate; add java expr accessor to the model
 * -------------------------------------------------------------------------------- */

// class GenFeatureSeqInitializer
gmfgen.GenFeatureSeqInitializer.newEOperation("getJavaExpressionFeatureInitializersList", gmfgen.GenFeatureValueSpec, 0, -1)
gmfgen.GenFeatureSeqInitializer.getJavaExpressionFeatureInitializersList.newEParameter("expressionProviders", gmfgen.GenExpressionProviderContainer, 0, 1)

// class Shortcuts
gmfgen.Shortcuts.containsShortcutsTo.eAnnotations[0].details[0].value = gmfgen.Shortcuts.containsShortcutsTo.eAnnotations[0].details[0].value.replace(
	"shortcutted",
	"added as a shortcut"
)
