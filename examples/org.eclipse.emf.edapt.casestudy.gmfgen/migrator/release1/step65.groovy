/* --------------------------------------------------------------------------------
 * revision 1.204
 * date: 2007-01-24 21:05:02 +0000;  author: atikhomirov;  lines: +5 -1;
 * need access to GenEditorGenerator from contribution items (i18n, #139733)
 * -------------------------------------------------------------------------------- */

// class GenContributionItem
gmfgen.GenContributionItem.newEReference("application", gmfgen.GenApplication, 0, 1, false, null)
gmfgen.GenContributionItem.application.changeable = false
gmfgen.GenContributionItem.application.'volatile' = true
gmfgen.GenContributionItem.application.'transient' = true
gmfgen.GenContributionItem.application.derived = true
gmfgen.GenContributionItem.application.resolveProxies = false

newOppositeReference(gmfgen.GenContributionManager.items, "owner", 0, 1, false)
