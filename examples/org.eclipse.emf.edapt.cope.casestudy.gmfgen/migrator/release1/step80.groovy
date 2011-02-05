/* --------------------------------------------------------------------------------
 * revision 1.219
 * date: 2007-04-27 11:23:38 +0000;  author: dstadnik;  lines: +6 -10;
 * combine label text access methods
 * -------------------------------------------------------------------------------- */

// enum LabelTextAccessMethod
gmfgen.newEEnum("LabelTextAccessMethod")
gmfgen.LabelTextAccessMethod.newEEnumLiteral("MESSAGE_FORMAT")
gmfgen.LabelTextAccessMethod.MESSAGE_FORMAT.value = 0
gmfgen.LabelTextAccessMethod.MESSAGE_FORMAT.instance = gmfgen.LabelTextAccessMethod.MESSAGE_FORMAT
gmfgen.LabelTextAccessMethod.MESSAGE_FORMAT.literal = "MESSAGE_FORMAT"
gmfgen.LabelTextAccessMethod.newEEnumLiteral("NATIVE")
gmfgen.LabelTextAccessMethod.NATIVE.value = 1
gmfgen.LabelTextAccessMethod.NATIVE.instance = gmfgen.LabelTextAccessMethod.NATIVE
gmfgen.LabelTextAccessMethod.NATIVE.literal = "NATIVE"
gmfgen.LabelTextAccessMethod.newEEnumLiteral("REGEXP")
gmfgen.LabelTextAccessMethod.REGEXP.value = 2
gmfgen.LabelTextAccessMethod.REGEXP.instance = gmfgen.LabelTextAccessMethod.REGEXP
gmfgen.LabelTextAccessMethod.REGEXP.literal = "REGEXP"
gmfgen.LabelTextAccessMethod.newEEnumLiteral("PRINTF")
gmfgen.LabelTextAccessMethod.PRINTF.value = 3
gmfgen.LabelTextAccessMethod.PRINTF.instance = gmfgen.LabelTextAccessMethod.PRINTF
gmfgen.LabelTextAccessMethod.PRINTF.literal = "PRINTF"

replaceEnum(
	gmfgen.LabelViewMethod,
	gmfgen.LabelTextAccessMethod,
	[gmfgen.LabelViewMethod.MESSAGE_FORMAT, gmfgen.LabelViewMethod.PRINTF, gmfgen.LabelViewMethod.NATIVE],
	[gmfgen.LabelTextAccessMethod.MESSAGE_FORMAT, gmfgen.LabelTextAccessMethod.PRINTF, gmfgen.LabelTextAccessMethod.NATIVE]
)

replaceEnum(
	gmfgen.LabelEditMethod,
	gmfgen.LabelTextAccessMethod,
	[gmfgen.LabelEditMethod.MESSAGE_FORMAT, gmfgen.LabelEditMethod.REGEXP, gmfgen.LabelEditMethod.NATIVE],
	[gmfgen.LabelTextAccessMethod.MESSAGE_FORMAT, gmfgen.LabelTextAccessMethod.REGEXP, gmfgen.LabelTextAccessMethod.NATIVE]
)

gmfgen.FeatureLabelModelFacet.viewPattern.eAnnotations[0].details[0].value = gmfgen.FeatureLabelModelFacet.viewPattern.eAnnotations[0].details[0].value.replace(
	"for java.text.MessageFormat ", "") + ", depends on view method"
gmfgen.FeatureLabelModelFacet.editPattern.eAnnotations[0].details[0].value = gmfgen.FeatureLabelModelFacet.editPattern.eAnnotations[0].details[0].value.replace(
	"for java.text.MessageFormat ", "").replace(
	";", ", depends on edit method;")
// revert step 79
