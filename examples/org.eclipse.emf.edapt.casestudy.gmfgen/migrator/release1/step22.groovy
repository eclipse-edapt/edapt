/* --------------------------------------------------------------------------------
 * revision 1.161
 * date: 2006-10-17 13:08:11 +0000;  author: dstadnik;  lines: +1 -2;
 * #160894 Move readOnly property from GenLabel to LabelFeatureModelFacet
 * -------------------------------------------------------------------------------- */

// class LabelModelFacet
combineFeature(
	[gmfgen.GenLabel.readOnly, gmfgen.GenChildLabelNode.labelReadOnly],
	[gmfgen.GenLabel.modelFacet, gmfgen.GenChildLabelNode.labelModelFacet]
)

assert gmfgen.GenLabel.readOnly == null
assert gmfgen.GenChildLabelNode.labelReadOnly == null
