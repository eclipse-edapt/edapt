/* --------------------------------------------------------------------------------
 * revision 1.230
 * date: 2007-05-22 17:27:36 +0000;  author: atikhomirov;  lines: +4 -5;
 * [123240] clean gmfgen uses of required plugins
 * -------------------------------------------------------------------------------- */

// class GenPlugin
gmfgen.GenPlugin.getRequiredPluginIDs.setName("getAllRequiredPlugins")
gmfgen.GenExpressionProviderBase.getRequiredPluginIDs.delete()
gmfgen.GenPlugin.getAllRequiredPlugins.eAnnotations[0].details[0].value = gmfgen.GenPlugin.getAllRequiredPlugins.eAnnotations[0].details[0].value.replace(
	" of generated plug-in",
	" (in form of plug-in identifiers), including those explicitly specified by user and those derived from referenced genmodels"
)
gmfgen.GenPlugin.getAllRequiredPlugins.eAnnotations[0].details[0].value = "Computed additional dependencies (in form of plug-in identifiers), including those explicitly specified by user and those derived from referenced genmodels"

pullFeature([gmfgen.GenExpressionInterpreter.requiredPluginIDs], gmfgen.GenExpressionProviderBase)
collectFeature(gmfgen.GenExpressionProviderBase.requiredPluginIDs, gmfgen.GenExpressionProviderContainer.providers)
moveFeature(gmfgen.GenExpressionProviderContainer.requiredPluginIDs, gmfgen.GenExpressionProviderContainer.editorGen)
moveFeature(gmfgen.GenEditorGenerator.requiredPluginIDs, gmfgen.GenEditorGenerator.plugin)
rename(gmfgen.GenPlugin.requiredPluginIDs, "requiredPlugins")
