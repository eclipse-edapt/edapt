/* --------------------------------------------------------------------------------
 * revision 1.245
 * date: 2008-05-05 21:06:41 +0000;  author: atikhomirov;  lines: +12 -6;
 * [228913] cleaned isCopy and java provider's getOperationName that are not in use any more; introduced fine-tuning options: use expressions body for java methods and whether to throw exception or fail silently (using reasonable/appropriate for the context default value), latter change obsoleted NoImplException and respective debug options
 * -------------------------------------------------------------------------------- */

// class GenJavaExpressionProvider
gmfgen.GenJavaExpressionProvider.newEAttribute("throwException", emf.EBoolean, 0, 1)
gmfgen.GenJavaExpressionProvider.throwException.defaultValueLiteral = "true"
gmfgen.GenJavaExpressionProvider.throwException.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenJavaExpressionProvider.throwException.eAnnotations[0].newEStringToStringMapEntry("documentaion", "Whether to generate default implementation that rises RuntimeException to signal unimplemented method")

gmfgen.GenJavaExpressionProvider.newEAttribute("injectExpressionBody", emf.EBoolean, 0, 1)
gmfgen.GenJavaExpressionProvider.injectExpressionBody.defaultValueLiteral = "false"
gmfgen.GenJavaExpressionProvider.injectExpressionBody.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenJavaExpressionProvider.injectExpressionBody.eAnnotations[0].newEStringToStringMapEntry("documentation", "When 'true', body of associated ValueExpression would get injected into Java code as-is, thus allowing to provide method implementations right within the model. Note, if body is empty, default implementation would be generated instead.")

gmfgen.GenJavaExpressionProvider.getOperationName.delete()

// class GenExpressionProviderContainer
gmfgen.GenExpressionProviderContainer.isCopy.delete()
