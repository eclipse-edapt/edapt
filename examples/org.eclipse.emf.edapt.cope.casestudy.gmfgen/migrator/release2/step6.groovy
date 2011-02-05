/* --------------------------------------------------------------------------------
 * revision 1.240
 * date: 2008-02-29 21:19:52 +0000;  author: atikhomirov;  lines: +0 -28;
 * [150177] phase IV, fix last use of qualified class names in ElementInitializers (via GMFGen genmodel java methods). Cleaned genmodel of methods no longer in use, few methods moved to ElementInitializer.ext (pending removal/refactoring)
 * -------------------------------------------------------------------------------- */

// class GenFeatureSeqInitializer
gmfgen.GenFeatureSeqInitializer.getJavaExpressionFeatureInitializersList.delete()
gmfgen.GenFeatureSeqInitializer.getElementQualifiedPackageInterfaceName.delete()
gmfgen.GenFeatureSeqInitializer.getElementClassAccessorName.delete()
gmfgen.GenFeatureSeqInitializer.getElementClassAccessor.delete()
gmfgen.GenFeatureSeqInitializer.getFeatureAccessor.delete()

// class GenExpressionProviderBase
gmfgen.GenExpressionProviderBase.getQualifiedTypeInstanceClassName.delete()
gmfgen.GenExpressionProviderBase.getQualifiedInstanceClassName.delete()

// class GenFeatureInitializer
gmfgen.GenFeatureInitializer.getFeatureQualifiedPackageInterfaceName.delete()
