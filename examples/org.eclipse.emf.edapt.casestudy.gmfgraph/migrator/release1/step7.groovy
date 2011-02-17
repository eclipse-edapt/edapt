/* --------------------------------------------------------------------------------
 * revision 1.30
 * date: 2007-05-28 13:33:36 +0000;  author: atikhomirov;  lines: +68 -58;
 * FigureDescriptor with explicit means to access children introduced into gmfgraph
 * -------------------------------------------------------------------------------- */

// class AbstractNode
extractSuperClass(
		gmfgraph.Node,
		[],
		gmfgraph,
		"AbstractNode",
		true,
		[gmfgraph.DiagramElement]
)

// class RealFigure
extractSuperClass(
		gmfgraph.ConnectionFigure,
		[],
		gmfgraph,
		"RealFigure",
		true,
		[gmfgraph.Figure]
)
gmfgraph.RealFigure.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.RealFigure.eAnnotations[0].newEStringToStringMapEntry("documentation", "This is exact/specific/concrete figure, unlike proxy/reference-nature FigureRef")

useSuperClass(
		gmfgraph.DecorationFigure,
		gmfgraph.RealFigure,
		[], [])

useSuperClass(
		gmfgraph.Shape,
		gmfgraph.RealFigure,
		[], [])

useSuperClass(
		gmfgraph.Label,
		gmfgraph.RealFigure,
		[], [])

useSuperClass(
		gmfgraph.LabeledContainer,
		gmfgraph.RealFigure,
		[], [])

useSuperClass(
		gmfgraph.CustomFigure,
		gmfgraph.RealFigure,
		[], [])
		
// class AbstractFigure
extractSuperClass(
	 	gmfgraph.RealFigure,
	 	[],
	 	gmfgraph,
	 	"AbstractFigure",
	 	true,
	 	[gmfgraph.Figure]
)
gmfgraph.AbstractFigure.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.AbstractFigure.eAnnotations[0].newEStringToStringMapEntry("documentation", "This is merely an implementation artifact to get only one base implementation of Figure attributes")

// reference children		
pushFeature(gmfgraph.Figure.children)

pushFeature(gmfgraph.AbstractFigure.children)

// attribute name
imitateSuperType(
		gmfgraph.Figure,
		gmfgraph.Identity)
		
pushFeature(gmfgraph.Figure._name)

pushFeature(gmfgraph.AbstractFigure._name)

generalizeAttribute(
		gmfgraph.RealFigure._name,
		0)
		
gmfgraph.RealFigure._name.iD = false

// reference figure
specializeReferenceType(
		gmfgraph.FigureRef.figure,
		gmfgraph.AbstractFigure)

specializeReferenceType(
		gmfgraph.FigureRef.figure,
		gmfgraph.RealFigure)
		
gmfgraph.FigureRef.figure.eAnnotations[0].details[0].value = gmfgraph.FigureRef.figure.eAnnotations[0].details[0].value.replace(
	"FigureMarker",
	"just Figure"
)

// reference figures
specializeReferenceType(
		gmfgraph.FigureGallery.figures,
		gmfgraph.AbstractFigure)
		
specializeReferenceType(
		gmfgraph.FigureGallery.figures,
		gmfgraph.RealFigure)

// class FigureMarker
specializeSuperType(
		gmfgraph.FigureRef,
		gmfgraph.FigureMarker,
		gmfgraph.AbstractFigure)
		
inlineSuperClass(
		gmfgraph.FigureMarker)
		
// class Figure
deleteFeature(gmfgraph.Figure.parent)

gmfgraph.Figure.eAnnotations[0].details[0].value = "Anything you could combine visual representation from. Ordinary GEF figures, custom-defined or references to defined elsewhere. " + gmfgraph.Figure.eAnnotations[0].details[0].value 

// class CustomFigure
gmfgraph.CustomFigure.customChildren.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.CustomFigure.customChildren.eAnnotations[0].newEStringToStringMapEntry("documentation", "Childrent enumerated with this feature are mere 'access points' to actual structure of the CustomFigure. They are not created, unlike those contained in regular Figure#children")

// class DiagramLabel
gmfgraph.DiagramLabel.newEAttribute("external", emf.EBoolean, 0, 1)
gmfgraph.DiagramLabel.external.changeable = false
gmfgraph.DiagramLabel.external.'volatile' = true
gmfgraph.DiagramLabel.external.'transient' = true
gmfgraph.DiagramLabel.external.derived = true

// class FigureHandle


// deletions
deleteFeature(gmfgraph.Node.nodeFigure)
deleteFeature(gmfgraph.Connection.connectionFigure)
deleteFeature(gmfgraph.CustomClass.bundleName)

gmfgraph.DiagramElement.find.delete()



generalizeReference(
			gmfgraph.FigureAccessor.typedFigure,
			gmfgraph.RealFigure)
			
makeContainment(gmfgraph.FigureAccessor.typedFigure)

/**
 * START custom coupled transaction
 */

gmfgraph.FigureAccessor.typedFigure.lowerBound = 1
for(fa in gmfgraph.FigureAccessor.allInstances) {
	def tf = fa.typedFigure
	if(tf == null) {
		tf = gmfgraph.CustomFigure.newInstance()
		tf.qualifiedClassName = "org.eclipse.draw2d.IFigure"
		fa.typedFigure = tf
	}
}

/**
 * END custom coupled transaction
 */


/**
 * START custom coupled transaction
 */
 
gmfgraph.newEClass("ChildAccess", [], false)
gmfgraph.ChildAccess.newEAttribute("accessor", emf.EString, 0, 1)
gmfgraph.ChildAccess.newEReference("figure", gmfgraph.Figure, 1, 1, false, null)
gmfgraph.ChildAccess.figure.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.ChildAccess.figure.eAnnotations[0].newEStringToStringMapEntry("documentation", "This may also point to RealFigure from FigureAccessor#typedFigure")

gmfgraph.newEClass("FigureDescriptor", [gmfgraph.Identity], false)
gmfgraph.FigureDescriptor.newEAnnotation("http://www.eclipse.org/emf/2002/GenModel")
gmfgraph.FigureDescriptor.newEReference("actualFigure", gmfgraph.Figure, 1, 1, true, null)
gmfgraph.FigureDescriptor.newEReference("accessors", gmfgraph.ChildAccess, 0, -1, true, null)
gmfgraph.FigureDescriptor.eAnnotations[0].newEStringToStringMapEntry("documentation", "The thing describes structure of a figure")

gmfgraph.ChildAccess.newEReference("owner", gmfgraph.FigureDescriptor, 1, 1, false, gmfgraph.FigureDescriptor.accessors)
gmfgraph.ChildAccess.owner.changeable = false
 
gmfgraph.Compartment.newEReference("accessor", gmfgraph.ChildAccess, 0, 1, false, null)
gmfgraph.DiagramLabel.newEReference("accessor", gmfgraph.ChildAccess, 0, 1, false, null)
gmfgraph.Node.newEReference("contentPane", gmfgraph.ChildAccess, 0, 1, false, null)

gmfgraph.FigureGallery.newEReference("descriptors", gmfgraph.FigureDescriptor, 0, -1, true, null)

gmfgraph.Figure.newEReference("descriptor", gmfgraph.FigureDescriptor, 0, 1, false, null)
gmfgraph.Figure.descriptor.changeable = false
gmfgraph.Figure.descriptor.'volatile' = true
gmfgraph.Figure.descriptor.'transient' = true
gmfgraph.Figure.descriptor.derived = true
gmfgraph.Figure.descriptor.resolveProxies = false

gmfgraph.DiagramLabel.newEReference("container", gmfgraph.ChildAccess, 0, 1, false, null)
 
deleteFeature(gmfgraph.FigureHandle.referencingElements)
gmfgraph.DiagramElement.figure.eType = gmfgraph.FigureDescriptor

gmfgraph.Figure.eSuperTypes.remove(gmfgraph.FigureHandle)		
gmfgraph.FigureAccessor.eSuperTypes.remove(gmfgraph.FigureHandle)

gmfgraph.FigureHandle.delete()


getToplevel = { handle ->
	if(handle.instanceOf(gmfgraph.FigureAccessor)) {
		handle = handle.getInverse(gmfgraph.CustomFigure.customChildren)
	}
	while(handle.getInverse(gmfgraph.RealFigure.children) != null) {
		handle = handle.getInverse(gmfgraph.RealFigure.children)
	}
	return handle
}

getOrCreateDescriptor = { toplevel ->
	def descriptor = toplevel.getInverse(gmfgraph.FigureDescriptor.actualFigure)
	if(descriptor == null) {
		descriptor = gmfgraph.FigureDescriptor.newInstance()
		def gallery = toplevel.getInverse(gmfgraph.FigureGallery.figures)
		descriptor.actualFigure = toplevel
		gallery.figures.remove(toplevel)
		gallery.descriptors.add(descriptor)
		descriptor.name = toplevel.name
	}
	return descriptor
}

findAccess = { descriptor, figure ->
	for(access in descriptor.accessors) {
		if(access.figure == figure) {
			return access
		}
	}
	return null
}

getOrCreateAccess = { descriptor, handle ->
	def figure = null
	if(handle.instanceOf(gmfgraph.FigureAccessor)) {
		figure = handle.typedFigure
	}
	else {
		figure = handle
	}	
	def access = findAccess(descriptor, figure)
	if(access == null) {
		access = gmfgraph.ChildAccess.newInstance()
		access.figure = figure
		descriptor.accessors.add(access)
	}
	return access
}

for(element in gmfgraph.DiagramElement.allInstances) {
	def handle = element.figure
	if(handle != null) {
		def toplevel = getToplevel(handle)
		def descriptor = getOrCreateDescriptor(toplevel)
		element.figure = descriptor
		if(toplevel != handle) {
			def access = getOrCreateAccess(descriptor, handle)
			if(element.instanceOf(gmfgraph.DiagramLabel)) {
				element.accessor = access
			}
			else if(element.instanceOf(gmfgraph.Compartment)) {
				element.accessor = access
			}
		}
	}
}

/**
 * END custom coupled transaction
 */
 
gmfgraph.nsURI = "http://www.eclipse.org/gmf/2006/GraphicalDefinition"