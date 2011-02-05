/* --------------------------------------------------------------------------------
 * revision 1.165
 * date: 2006-10-19 14:13:40 +0000;  author: radvorak;  lines: +1 -1;
 * [161489] [Duplicate] Correct gmfgen constraint on GenFeatureValueSpec according to EMFT OCL #151234
 * -------------------------------------------------------------------------------- */

// class GenFeatureInitializer
gmfgen.GenFeatureInitializer.feature.eAnnotations[1].details[0].value = gmfgen.GenFeatureInitializer.feature.eAnnotations[1].details[0].value.replace(
	" feature ",
	" i.feature "
)
