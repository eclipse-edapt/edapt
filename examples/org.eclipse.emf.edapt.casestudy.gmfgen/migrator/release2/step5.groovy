/* --------------------------------------------------------------------------------
 * revision 1.239
 * date: 2008-02-27 17:30:20 +0000;  author: atikhomirov;  lines: +18 -0;
 * [150177] phase II, generated facility to perform metamodel operations (like instanceof or metaclass accessors) with dynamic models, new gmfgen options to parameterize codegen.
 * -------------------------------------------------------------------------------- */

// class DynamicModelAccess
gmfgen.newEClass("DynamicModelAccess", [], false)
gmfgen.DynamicModelAccess.newEOperation("getQualifiedClassName", emf.EString, 0, 1)
gmfgen.DynamicModelAccess.newEReference("editorGen", gmfgen.GenEditorGenerator, 0, 1, false, null)
gmfgen.DynamicModelAccess.editorGen.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.DynamicModelAccess.editorGen.eAnnotations[0].newEStringToStringMapEntry("suppressedSetVisibility", "true")
gmfgen.DynamicModelAccess.newEAttribute("packageName", emf.EString, 0, 1)
gmfgen.DynamicModelAccess.newEAttribute("className", emf.EString, 0, 1)
gmfgen.DynamicModelAccess.className.defaultValueLiteral = "MetaModelFacility"

// class GenEditorGenerator
gmfgen.GenEditorGenerator.newEReference("modelAccess", gmfgen.DynamicModelAccess, 0, 1, true, gmfgen.DynamicModelAccess.editorGen)
gmfgen.GenEditorGenerator.modelAccess.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgen.GenEditorGenerator.modelAccess.eAnnotations[0].newEStringToStringMapEntry("documentation", "If present, specifies dynamic access to domain model(s), without using generated Java code")
