/* --------------------------------------------------------------------------------
 * revision 1.218
 * date: 2007-04-26 14:11:51 +0000;  author: dstadnik;  lines: +14 -2;
 * additional methods to produce label text and parse user input
 * -------------------------------------------------------------------------------- */

// enum LabelViewMethod
gmfgen.newEEnum("LabelViewMethod")
gmfgen.LabelViewMethod.newEEnumLiteral("MESSAGE_FORMAT")
gmfgen.LabelViewMethod.MESSAGE_FORMAT.value = 0
gmfgen.LabelViewMethod.MESSAGE_FORMAT.instance = gmfgen.LabelViewMethod.MESSAGE_FORMAT
gmfgen.LabelViewMethod.MESSAGE_FORMAT.literal = "MESSAGE_FORMAT"
gmfgen.LabelViewMethod.newEEnumLiteral("PRINTF")
gmfgen.LabelViewMethod.PRINTF.value = 1
gmfgen.LabelViewMethod.PRINTF.instance = gmfgen.LabelViewMethod.PRINTF
gmfgen.LabelViewMethod.PRINTF.literal = "PRINTF"
gmfgen.LabelViewMethod.newEEnumLiteral("NATIVE")
gmfgen.LabelViewMethod.NATIVE.value = 2
gmfgen.LabelViewMethod.NATIVE.instance = gmfgen.LabelViewMethod.NATIVE
gmfgen.LabelViewMethod.NATIVE.literal = "NATIVE"

// enum LabelEditMethod
gmfgen.newEEnum("LabelEditMethod")
gmfgen.LabelEditMethod.newEEnumLiteral("MESSAGE_FORMAT")
gmfgen.LabelEditMethod.MESSAGE_FORMAT.value = 0
gmfgen.LabelEditMethod.MESSAGE_FORMAT.instance = gmfgen.LabelEditMethod.MESSAGE_FORMAT
gmfgen.LabelEditMethod.MESSAGE_FORMAT.literal = "MESSAGE_FORMAT"
gmfgen.LabelEditMethod.newEEnumLiteral("REGEXP")
gmfgen.LabelEditMethod.REGEXP.value = 1
gmfgen.LabelEditMethod.REGEXP.instance = gmfgen.LabelEditMethod.REGEXP
gmfgen.LabelEditMethod.REGEXP.literal = "REGEXP"
gmfgen.LabelEditMethod.newEEnumLiteral("NATIVE")
gmfgen.LabelEditMethod.NATIVE.value = 2
gmfgen.LabelEditMethod.NATIVE.instance = gmfgen.LabelEditMethod.NATIVE
gmfgen.LabelEditMethod.NATIVE.literal = "NATIVE"

// class FeatureLabelModelFacet
gmfgen.FeatureLabelModelFacet.newEAttribute("editMethod", gmfgen.LabelEditMethod, 0, 1)
gmfgen.FeatureLabelModelFacet.newEAttribute("viewMethod", gmfgen.LabelViewMethod, 0, 1)
