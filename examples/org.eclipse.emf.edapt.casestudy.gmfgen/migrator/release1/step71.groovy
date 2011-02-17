/* --------------------------------------------------------------------------------
 * revision 1.210
 * date: 2007-02-08 19:31:57 +0000;  author: atikhomirov;  lines: +4 -14;
 * get rid of GenRuleContainerBase
 * -------------------------------------------------------------------------------- */

// class GenMetricContainer
inlineSuperClass(gmfgen.GenRuleContainerBase)

rename(gmfgen.GenMetricContainer.editor, "editorGen")

volatileToOpposite(gmfgen.GenMetricContainer.editorGen, gmfgen.GenEditorGenerator.metrics)

gmfgen.GenMetricContainer.editorGen.eAnnotations[0].delete()
