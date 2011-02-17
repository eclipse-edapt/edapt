/* --------------------------------------------------------------------------------
 * revision 1.236
 * date: 2007-09-10 18:42:05 +0000;  author: dstadnik;  lines: +7 -0;
 * expose more ops in gmfgen
 * -------------------------------------------------------------------------------- */

// class GenFeatureSeqInitializer
gmfgen.GenFeatureSeqInitializer.newEOperation("getElementClassAccessor", emf.EString, 1, 1)
gmfgen.GenFeatureSeqInitializer.newEOperation("getFeatureAccessor", emf.EString, 1, 1)
gmfgen.GenFeatureSeqInitializer.getFeatureAccessor.newEParameter("ftInitializer", gmfgen.GenFeatureInitializer, 0, 1)

// class GenElementInitializer
gmfgen.GenElementInitializer.newEOperation("getInitializerFieldName", emf.EString, 1, 1)
gmfgen.GenElementInitializer.getInitializerFieldName.newEParameter("elementID", emf.EString, 0, 1)
