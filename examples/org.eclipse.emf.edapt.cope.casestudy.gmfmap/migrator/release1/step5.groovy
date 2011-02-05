/* --------------------------------------------------------------------------------
 * revision 1.48
 * date: 2006-10-19 14:13:36 +0000;  author: radvorak;  lines: +1 -1;
 * [161489] [Duplicate] Correct gmfgen constraint on GenFeatureValueSpec according to EMFT OCL #151234
 * -------------------------------------------------------------------------------- */

// modified OCL constraint
mappings.FeatureInitializer.feature.eAnnotations[1].details[0].value = mappings.FeatureInitializer.feature.eAnnotations[1].details[0].value.replace(" feature ", " i.feature ")
