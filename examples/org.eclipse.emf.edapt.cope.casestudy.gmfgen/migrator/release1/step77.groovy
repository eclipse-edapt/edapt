/* --------------------------------------------------------------------------------
 * revision 1.216
 * date: 2007-04-11 08:10:24 +0000;  author: dstadnik;  lines: +3 -3;
 * [181778] Generate standalone creation commands for reference based links
 * -------------------------------------------------------------------------------- */

// class TypeLinkModelFacet
pullFeature([gmfgen.TypeLinkModelFacet.createCommandClassName], gmfgen.LinkModelFacet)
collectFeature(gmfgen.LinkModelFacet.createCommandClassName, gmfgen.GenLink.modelFacet)

pullOperation([gmfgen.TypeLinkModelFacet.getCreateCommandQualifiedClassName], gmfgen.LinkModelFacet)
gmfgen.GenLink.eOperations.add(gmfgen.LinkModelFacet.getCreateCommandQualifiedClassName)
