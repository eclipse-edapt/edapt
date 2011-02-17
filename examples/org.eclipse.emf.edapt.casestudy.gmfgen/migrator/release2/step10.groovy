/* --------------------------------------------------------------------------------
 * revision 1.244
 * date: 2008-04-18 14:43:23 +0000;  author: atikhomirov;  lines: +33 -156;
 * with [221352] resolved, we are safe to use readonly backreferences again, without suppressSetVisibility hack
 * -------------------------------------------------------------------------------- */

// class GenDiagram
gmfgen.GenDiagram.editorGen.changeable = false
gmfgen.GenDiagram.editorGen.eAnnotations[0].delete()

// class GenEditorView
gmfgen.GenEditorView.editorGen.changeable = false
gmfgen.GenEditorView.editorGen.eAnnotations[0].delete()

// class GenPreferencePage
gmfgen.GenPreferencePage.parent.changeable = false
gmfgen.GenPreferencePage.parent.eAnnotations[0].delete()

// class GenDiagramUpdater
gmfgen.GenDiagramUpdater.editorGen.changeable = false
gmfgen.GenDiagramUpdater.editorGen.eAnnotations[0].delete()

// class GenPlugin
gmfgen.GenPlugin.editorGen.changeable = false
gmfgen.GenPlugin.editorGen.eAnnotations[0].delete()

// class DynamicModelAccess
gmfgen.DynamicModelAccess.editorGen.changeable = false
gmfgen.DynamicModelAccess.editorGen.eAnnotations[0].delete()

// class Behaviour
gmfgen.Behaviour.subject.changeable = false
gmfgen.Behaviour.subject.eAnnotations[0].delete()

// class GenTopLevelNode
gmfgen.GenTopLevelNode.diagram.changeable = false
gmfgen.GenTopLevelNode.diagram.eAnnotations[0].delete()

// class GenChildNode
gmfgen.GenChildNode.diagram.changeable = false
gmfgen.GenChildNode.diagram.eAnnotations[0].delete()

// class GenCompartment
gmfgen.GenCompartment.diagram.changeable = false
gmfgen.GenCompartment.diagram.eAnnotations[0].delete()

// class GenLink
gmfgen.GenLink.diagram.changeable = false
gmfgen.GenLink.diagram.eAnnotations[0].delete()

// class GenNodeLabel
gmfgen.GenNodeLabel.node.changeable = false
gmfgen.GenNodeLabel.node.eAnnotations[0].delete()

// class Palette
gmfgen.Palette.diagram.changeable = false
gmfgen.Palette.diagram.eAnnotations[0].delete()

// class ToolGroupItem
gmfgen.ToolGroupItem.group.changeable = false
gmfgen.ToolGroupItem.group.eAnnotations[0].delete()

// class GenFeatureSeqInitializer
gmfgen.GenFeatureSeqInitializer.creatingInitializer.changeable = false
gmfgen.GenFeatureSeqInitializer.creatingInitializer.eAnnotations[0].delete()

// class GenFeatureInitializer
gmfgen.GenFeatureInitializer.featureSeqInitializer.changeable = false
gmfgen.GenFeatureInitializer.featureSeqInitializer.eAnnotations[0].delete()

// class GenAuditRoot
gmfgen.GenAuditRoot.editorGen.changeable = false
gmfgen.GenAuditRoot.editorGen.eAnnotations[0].delete()

// class GenAuditContainer
gmfgen.GenAuditContainer.root.changeable = false
gmfgen.GenAuditContainer.root.eAnnotations[0].delete()

// class GenAuditRule
gmfgen.GenAuditRule.root.changeable = false
gmfgen.GenAuditRule.root.eAnnotations[0].delete()

// class GenMetricContainer
gmfgen.GenMetricContainer.editorGen.changeable = false
gmfgen.GenMetricContainer.editorGen.eAnnotations[0].delete()

// class GenExpressionProviderContainer
gmfgen.GenExpressionProviderContainer.editorGen.changeable = false
gmfgen.GenExpressionProviderContainer.editorGen.eAnnotations[0].delete()

// class GenExpressionProviderBase
gmfgen.GenExpressionProviderBase._container.changeable = false
gmfgen.GenExpressionProviderBase._container.eAnnotations[0].delete()

// class GenNavigator
gmfgen.GenNavigator.editorGen.changeable = false
gmfgen.GenNavigator.editorGen.eAnnotations[0].delete()

// class GenNavigatorChildReference
gmfgen.GenNavigatorChildReference.navigator.changeable = false
gmfgen.GenNavigatorChildReference.navigator.eAnnotations[0].delete()

// class GenNavigatorPathSegment
gmfgen.GenNavigatorPathSegment.path.changeable = false
gmfgen.GenNavigatorPathSegment.path.eAnnotations[0].delete()

// class GenPropertySheet
gmfgen.GenPropertySheet.editorGen.changeable = false
gmfgen.GenPropertySheet.editorGen.eAnnotations[0].delete()

// class GenPropertyTab
gmfgen.GenPropertyTab.sheet.changeable = false
gmfgen.GenPropertyTab.sheet.eAnnotations[0].delete()

// class GenPropertyTabFilter
gmfgen.GenPropertyTabFilter.tab.changeable = false
gmfgen.GenPropertyTabFilter.tab.eAnnotations[0].delete()

// class GenContributionItem
gmfgen.GenContributionItem.owner.changeable = false
gmfgen.GenContributionItem.owner.eAnnotations[0].delete()

// class GenApplication
gmfgen.GenApplication.editorGen.changeable = false
gmfgen.GenApplication.editorGen.eAnnotations[0].delete()

// class ToolGroup
gmfgen.ToolGroup.palette.eAnnotations[0].delete()
