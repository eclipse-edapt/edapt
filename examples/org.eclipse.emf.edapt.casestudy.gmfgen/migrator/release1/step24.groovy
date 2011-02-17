/* --------------------------------------------------------------------------------
 * revision 1.163
 * date: 2006-10-18 09:56:50 +0000;  author: dstadnik;  lines: +2 -1;
 * #161380 move readOnly back to GenLabel
 * -------------------------------------------------------------------------------- */

// class LabelModelFacet
propagateFeature(
	gmfgen.LabelModelFacet.readOnly,
	[gmfgen.GenLabel.modelFacet, gmfgen.GenChildLabelNode.labelModelFacet]
)

rename(gmfgen.GenChildLabelNode.readOnly, "labelReadOnly")

// revert step 22
