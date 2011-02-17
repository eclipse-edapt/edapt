/* --------------------------------------------------------------------------------
 * revision 1.45
 * date: 2006-09-07 12:48:32 +0000;  author: ashatalin;  lines: +2 -2;
 * [156500] mgolubev - Incorrect constraint specified for ChildReference in gmfmap model
 * -------------------------------------------------------------------------------- */

// modified OCL constraints - overwrites step 1
mappings.ChildReference.eAnnotations[4].details[0].value = mappings.ChildReference.eAnnotations[4].details[0].value.replace("NSEW", "NONE")
mappings.ChildReference.eAnnotations[5].details[0].value = mappings.ChildReference.eAnnotations[5].details[0].value.replace("NSEW", "NONE")
	
