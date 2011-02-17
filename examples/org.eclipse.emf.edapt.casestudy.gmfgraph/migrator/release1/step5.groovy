/* --------------------------------------------------------------------------------
 * revision 1.28
 * date: 2006-11-02 22:50:23 +0000;  author: atikhomirov;  lines: +10 -10;
 * reverted explicit EObject superclass in the metamodel since it's not being utilized in new templates
 * -------------------------------------------------------------------------------- */

// cancelled out by step 4
gmfgraph.Figure.eSuperTypes.remove(emf.EObject)
gmfgraph.CustomAttribute.eSuperTypes.remove(emf.EObject)
gmfgraph.Color.eSuperTypes.remove(emf.EObject)
gmfgraph.Font.eSuperTypes.remove(emf.EObject)
gmfgraph.Point.eSuperTypes.remove(emf.EObject)
gmfgraph.Dimension.eSuperTypes.remove(emf.EObject)
gmfgraph.Insets.eSuperTypes.remove(emf.EObject)
gmfgraph.Border.eSuperTypes.remove(emf.EObject)
gmfgraph.LayoutData.eSuperTypes.remove(emf.EObject)
gmfgraph.Layout.eSuperTypes.remove(emf.EObject)
