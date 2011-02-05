/* --------------------------------------------------------------------------------
 * revision 1.49
 * date: 2006-10-19 15:21:57 +0000;  author: dstadnik;  lines: +17 -7;
 * [161380] Inroduce label mapping subclasses
 * -------------------------------------------------------------------------------- */

// class FeatureLabelMapping
extractSubClass(
		mappings.LabelMapping,
		mappings.LabelMapping.features,
		"FeatureLabelMapping")

pushFeature(mappings.LabelMapping.viewPattern)
pushFeature(mappings.LabelMapping.editPattern)

mappings.FeatureLabelMapping.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.FeatureLabelMapping.eAnnotations[0].newEStringToStringMapEntry("documentation", "Label based on feature(s) from domain model")

// class DesignLabelMapping
mappings.newEClass("DesignLabelMapping", [mappings.LabelMapping], false)
mappings.DesignLabelMapping.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.DesignLabelMapping.eAnnotations[0].newEStringToStringMapEntry("documentation", "Label based on DescriptionStyle from notation model")

// class LabelMapping
mappings.LabelMapping.eAnnotations[0].details[0].value = "Label definition; text is taken from the graph model; no editing support; user may contribute custom parser"
