/* --------------------------------------------------------------------------------
 * revision 1.171
 * date: 2006-11-17 16:52:18 +0000;  author: atikhomirov;  lines: +0 -10;
 * removed stale attributes from gmfgen model.
 * migration resource eagerly treats any resource with newest nsURI as potential candidate for missed attributes
 * -------------------------------------------------------------------------------- */

// class ProviderClassNames
deleteFeature(gmfgen.ProviderClassNames.propertyProviderClassName)
deleteFeature(gmfgen.ProviderClassNames.paletteProviderPriority)
deleteFeature(gmfgen.ProviderClassNames.propertyProviderPriority)
deleteFeature(gmfgen.ProviderClassNames.paletteProviderClassName)

gmfgen.ProviderClassNames.getPropertyProviderQualifiedClassName.delete()
gmfgen.ProviderClassNames.getPaletteProviderQualifiedClassName.delete()
