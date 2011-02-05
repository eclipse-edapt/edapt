/* --------------------------------------------------------------------------------
 * revision 1.52
 * date: 2007-04-27 11:23:42 +0000;  author: dstadnik;  lines: +6 -10;
 * combine label text access methods
 * -------------------------------------------------------------------------------- */

// enum LabelTextAccessMethod
mappings.newEEnum("LabelTextAccessMethod")
mappings.LabelTextAccessMethod.newEEnumLiteral("MESSAGE_FORMAT")
mappings.LabelTextAccessMethod.MESSAGE_FORMAT.value = 0
mappings.LabelTextAccessMethod.MESSAGE_FORMAT.instance = mappings.LabelTextAccessMethod.MESSAGE_FORMAT
mappings.LabelTextAccessMethod.MESSAGE_FORMAT.literal = "MESSAGE_FORMAT"
mappings.LabelTextAccessMethod.newEEnumLiteral("NATIVE")
mappings.LabelTextAccessMethod.NATIVE.value = 1
mappings.LabelTextAccessMethod.NATIVE.instance = mappings.LabelTextAccessMethod.NATIVE
mappings.LabelTextAccessMethod.NATIVE.literal = "NATIVE"
mappings.LabelTextAccessMethod.newEEnumLiteral("REGEXP")
mappings.LabelTextAccessMethod.REGEXP.value = 2
mappings.LabelTextAccessMethod.REGEXP.instance = mappings.LabelTextAccessMethod.REGEXP
mappings.LabelTextAccessMethod.REGEXP.literal = "REGEXP"
mappings.LabelTextAccessMethod.newEEnumLiteral("PRINTF")
mappings.LabelTextAccessMethod.PRINTF.value = 3
mappings.LabelTextAccessMethod.PRINTF.instance = mappings.LabelTextAccessMethod.PRINTF
mappings.LabelTextAccessMethod.PRINTF.literal = "PRINTF"

replaceEnum(
		mappings.LabelEditMethod,
		mappings.LabelTextAccessMethod,
		[mappings.LabelEditMethod.MESSAGE_FORMAT, mappings.LabelEditMethod.REGEXP, mappings.LabelEditMethod.NATIVE],
		[mappings.LabelTextAccessMethod.MESSAGE_FORMAT, mappings.LabelTextAccessMethod.REGEXP, mappings.LabelTextAccessMethod.NATIVE]
)
		
replaceEnum(
		mappings.LabelViewMethod,
		mappings.LabelTextAccessMethod,
		[mappings.LabelViewMethod.MESSAGE_FORMAT, mappings.LabelViewMethod.PRINTF, mappings.LabelViewMethod.NATIVE],
		[mappings.LabelTextAccessMethod.MESSAGE_FORMAT, mappings.LabelTextAccessMethod.PRINTF, mappings.LabelTextAccessMethod.NATIVE]
)
