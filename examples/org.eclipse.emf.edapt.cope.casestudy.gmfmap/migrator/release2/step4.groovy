/* --------------------------------------------------------------------------------
 * revision 1.58
 * date: 2008-04-18 14:43:25 +0000;  author: atikhomirov;  lines: +6 -24;
 * with [221352] resolved, we are safe to use readonly backreferences again, without suppressSetVisibility hack
 * -------------------------------------------------------------------------------- */

// cancelled out by step 1

// class CompartmentMapping
mappings.CompartmentMapping.parentNode.changeable = false
mappings.CompartmentMapping.parentNode.eAnnotations[0].delete()

// class LabelMapping
mappings.LabelMapping.mapEntry.changeable = false
mappings.LabelMapping.mapEntry.eAnnotations[0].delete()

// class LinkConstraints
mappings.LinkConstraints.linkMapping.changeable = false
mappings.LinkConstraints.linkMapping.eAnnotations[1].delete()

// class FeatureSeqInitializer
mappings.FeatureSeqInitializer.creatingInitializer.changeable = false
mappings.FeatureSeqInitializer.creatingInitializer.eAnnotations[0].delete()

// class FeatureInitializer
mappings.FeatureInitializer.featureSeqInitializer.changeable = false
mappings.FeatureInitializer.featureSeqInitializer.eAnnotations[0].delete()
