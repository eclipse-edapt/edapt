/* --------------------------------------------------------------------------------
 * revision 1.203
 * date: 2007-01-17 13:12:34 +0000;  author: ashatalin;  lines: +6 -4;
 * NodeItemSemanticEditPolicy template reimplemented using xpand.
 * -------------------------------------------------------------------------------- */

// class GenLink
rename(gmfgen.GenLink.getSources, "getAssistantSources")
rename(gmfgen.GenLink.getTargets, "getAssistantTargets")

// class LinkModelFacet
rename(gmfgen.LinkModelFacet.getSourceTypes, "getAssistantSourceTypes")
rename(gmfgen.LinkModelFacet.getTargetTypes, "getAssistantTargetTypes")

gmfgen.LinkModelFacet.newEOperation("getSourceType", genmodel.GenClass, 0, 1)
gmfgen.LinkModelFacet.newEOperation("getTargetType", genmodel.GenClass, 0, 1)
