moveClass = history.Move
sourceReference = moveClass.newEReference()
sourceReference.name = "source"
sourceReference.lowerBound = 1
eObjectClass = emf.EObject
sourceReference.eType = eObjectClass
setClass = history.Set
oldDataValueAttribute = setClass.newEAttribute()
oldDataValueAttribute.name = "oldDataValue"
eStringDataType = emf.EString
oldDataValueAttribute.eType = eStringDataType
oldReferenceValueReference = setClass.newEReference()
oldReferenceValueReference.name = "oldReferenceValue"
oldReferenceValueReference.eType = eObjectClass
oldValueAttribute = setClass.newEAttribute()
oldValueAttribute.name = "oldValue"
oldValueAttribute.'volatile' = true
oldValueAttribute.'transient' = true
oldValueAttribute.derived = true
eJavaObjectDataType = emf.EJavaObject
oldValueAttribute.eType = eJavaObjectDataType
eAnnotation = oldValueAttribute.newEAnnotation()
eAnnotation.source = "http://www.eclipse.org/gmt/cope/codegen"
eStringToStringMapEntry = eAnnotation.newEStringToStringMapEntry()
eStringToStringMapEntry.key = "get"
eStringToStringMapEntry.value = ""
eStringToStringMapEntry2 = eAnnotation.newEStringToStringMapEntry()
eStringToStringMapEntry2.key = "set"
eStringToStringMapEntry2.value = ""
deleteClass = history.Delete
changesReference = deleteClass.newEReference()
changesReference.name = "changes"
changesReference.upperBound = -1
changesReference.containment = true
valueChangeClass = history.ValueChange
changesReference.eType = valueChangeClass