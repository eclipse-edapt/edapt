/* --------------------------------------------------------------------------------
 * revision 1.55
 * date: 2008-03-04 16:50:29 +0000;  author: atikhomirov;  lines: +24 -5;
 * with EMF 2.4M5, it's no longer possible to use read-only opposite for containments (due to changes in SetCommand), regenerated to use different approach
 * -------------------------------------------------------------------------------- */

// cancelled out by step 4

// class FeatureInitializer
mappings.FeatureInitializer.featureSeqInitializer.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.FeatureInitializer.featureSeqInitializer.eAnnotations[0].newEStringToStringMapEntry("suppressedSetVisibility", "true")
mappings.FeatureInitializer.featureSeqInitializer.changeable = true
mappings.FeatureInitializer.featureSeqInitializer.resolveProxies = false

// class FeatureSeqInitializer
mappings.FeatureSeqInitializer.creatingInitializer.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.FeatureSeqInitializer.creatingInitializer.eAnnotations[0].newEStringToStringMapEntry("suppressedSetVisibility", "true")
mappings.FeatureSeqInitializer.creatingInitializer.changeable = true
mappings.FeatureSeqInitializer.creatingInitializer.resolveProxies = false

// class LinkConstraints
mappings.LinkConstraints.linkMapping.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.LinkConstraints.linkMapping.eAnnotations[1].newEStringToStringMapEntry("suppressedSetVisibility", "true")
mappings.LinkConstraints.linkMapping.changeable = true
mappings.LinkConstraints.linkMapping.resolveProxies = false

// class LabelMapping
mappings.LabelMapping.mapEntry.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.LabelMapping.mapEntry.eAnnotations[0].newEStringToStringMapEntry("suppressedSetVisibility", "true")
mappings.LabelMapping.mapEntry.changeable = true
mappings.LabelMapping.mapEntry.resolveProxies = false

// class CompartmentMapping
mappings.CompartmentMapping.parentNode.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
mappings.CompartmentMapping.parentNode.eAnnotations[0].newEStringToStringMapEntry("suppressedSetVisibility", "true")
mappings.CompartmentMapping.parentNode.changeable = true
mappings.CompartmentMapping.parentNode.resolveProxies = false
