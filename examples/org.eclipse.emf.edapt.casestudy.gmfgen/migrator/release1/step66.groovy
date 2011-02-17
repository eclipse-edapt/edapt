/* --------------------------------------------------------------------------------
 * revision 1.205
 * date: 2007-01-25 16:57:54 +0000;  author: ashatalin;  lines: +11 -0;
 * Revriting BaseItemSemanticEditPolicy to xpand
 * -------------------------------------------------------------------------------- */

// class ValueExpression
gmfgen.ValueExpression.newEOperation("getBodyString", emf.EString, 0, 1)
gmfgen.ValueExpression.getBodyString.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.ValueExpression.getBodyString.eAnnotations[0].newEStringToStringMapEntry("documentation", "Returns valid String literal for the given <code>String</code> as it should appear in java source code.")

// class GenExpressionProviderBase
gmfgen.GenExpressionProviderBase.newEOperation("getQualifiedTypeInstanceClassName", emf.EString, 0, 1)
gmfgen.GenExpressionProviderBase.getQualifiedTypeInstanceClassName.newEParameter("genTypedElement", genmodel.GenTypedElement, 0, 1)

gmfgen.GenExpressionProviderBase.newEOperation("getQualifiedInstanceClassName", emf.EString, 0, 1)
gmfgen.GenExpressionProviderBase.getQualifiedInstanceClassName.newEParameter("genClassifier", genmodel.GenClassifier, 0, 1)
