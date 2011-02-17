createClass = history.Create
initializerChangeClass = history.InitializerChange
createClass.eSuperTypes.add(initializerChangeClass)
deleteClass = history.Delete
deleteClass.eSuperTypes.add(initializerChangeClass)
