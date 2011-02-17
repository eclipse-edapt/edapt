/* --------------------------------------------------------------------------------
 * revision 1.26
 * date: 2006-09-01 17:49:34 +0000;  author: atikhomirov;  lines: +4 -0;
 * [155230] mgolubev - Provide means to specify default size for figure
 * -------------------------------------------------------------------------------- */

// class DefaultSizeFacet
gmfgraph.newEClass("DefaultSizeFacet", [gmfgraph.VisualFacet], false)
gmfgraph.DefaultSizeFacet.newEReference("defaultSize", gmfgraph.Dimension, 0, 1, true, null)
