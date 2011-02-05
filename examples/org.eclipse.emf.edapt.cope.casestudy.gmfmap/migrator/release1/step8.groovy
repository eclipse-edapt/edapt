/* --------------------------------------------------------------------------------
 * revision 1.51
 * date: 2007-04-26 15:39:50 +0000;  author: dstadnik;  lines: +14 -2;
 * additional methods to produce label text and parse user input
 * -------------------------------------------------------------------------------- */

// enum LabelEditMethod
mappings.newEEnum("LabelEditMethod")
mappings.LabelEditMethod.newEEnumLiteral("MESSAGE_FORMAT")
mappings.LabelEditMethod.MESSAGE_FORMAT.value = 0
mappings.LabelEditMethod.MESSAGE_FORMAT.instance = mappings.LabelEditMethod.MESSAGE_FORMAT
mappings.LabelEditMethod.MESSAGE_FORMAT.literal = "MESSAGE_FORMAT"
mappings.LabelEditMethod.newEEnumLiteral("REGEXP")
mappings.LabelEditMethod.REGEXP.value = 1
mappings.LabelEditMethod.REGEXP.instance = mappings.LabelEditMethod.REGEXP
mappings.LabelEditMethod.REGEXP.literal = "REGEXP"
mappings.LabelEditMethod.newEEnumLiteral("NATIVE")
mappings.LabelEditMethod.NATIVE.value = 2
mappings.LabelEditMethod.NATIVE.instance = mappings.LabelEditMethod.NATIVE
mappings.LabelEditMethod.NATIVE.literal = "NATIVE"

// enum LabelViewMethod
mappings.newEEnum("LabelViewMethod")
mappings.LabelViewMethod.newEEnumLiteral("MESSAGE_FORMAT")
mappings.LabelViewMethod.MESSAGE_FORMAT.value = 0
mappings.LabelViewMethod.MESSAGE_FORMAT.instance = mappings.LabelViewMethod.MESSAGE_FORMAT
mappings.LabelViewMethod.MESSAGE_FORMAT.literal = "MESSAGE_FORMAT"
mappings.LabelViewMethod.newEEnumLiteral("PRINTF")
mappings.LabelViewMethod.PRINTF.value = 1
mappings.LabelViewMethod.PRINTF.instance = mappings.LabelViewMethod.PRINTF
mappings.LabelViewMethod.PRINTF.literal = "PRINTF"
mappings.LabelViewMethod.newEEnumLiteral("NATIVE")
mappings.LabelViewMethod.NATIVE.value = 2
mappings.LabelViewMethod.NATIVE.instance = mappings.LabelViewMethod.NATIVE
mappings.LabelViewMethod.NATIVE.literal = "NATIVE"

// class FeatureLabelMapping
mappings.FeatureLabelMapping.newEAttribute("editMethod", mappings.LabelEditMethod, 0, 1)
mappings.FeatureLabelMapping.newEAttribute("viewMethod", mappings.LabelViewMethod, 0, 1)

mappings.FeatureLabelMapping.viewPattern.eAnnotations[0].details[0].value = mappings.FeatureLabelMapping.viewPattern.eAnnotations[0].details[0].value.replace("for java.text.MessageFormat ", "") + ", depends on view method"
mappings.FeatureLabelMapping.editPattern.eAnnotations[0].details[0].value = mappings.FeatureLabelMapping.editPattern.eAnnotations[0].details[0].value.replace("for java.text.MessageFormat ", "").replace(";", ", depends on edit method;")
