@rem ***************************************************************************
@rem Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
@rem All rights reserved. This program and the accompanying materials
@rem are made available under the terms of the Eclipse Public License 2.0
@rem which accompanies this distribution, and is available at
@rem https://www.eclipse.org/legal/epl-2.0/
@rem
@rem SPDX-License-Identifier: EPL-2.0
@rem
@rem Contributors:
@rem     BMW Car IT - Initial API and implementation
@rem     Technische Universitaet Muenchen - Major refactoring and extension
@rem ***************************************************************************
FOR %%i IN (Add, Remove, Delete, Move, CreateChild, Set) DO copy /y %%i.gif ..\..\..\..\de.bmw.emf.evolution.declaration.edit\icons\full\obj16\%%iStatement.gif
copy /y MigrationInstance.gif ..\..\..\..\de.bmw.emf.evolution.declaration.edit\icons\full\obj16\Migration.gif
copy /y ExtensionInstance.gif ..\..\..\..\de.bmw.emf.evolution.declaration.edit\icons\full\obj16\Extension.gif
copy /y OperationInstance.gif ..\..\..\..\de.bmw.emf.evolution.declaration.edit\icons\full\obj16\Operation.gif
copy /y OperationInstance.gif ..\..\..\..\de.bmw.emf.evolution.declaration.edit\icons\full\obj16\Operations.gif
copy /y ParameterInstance.gif ..\..\..\..\de.bmw.emf.evolution.declaration.edit\icons\full\obj16\Parameter.gif
copy /y VariableInstance.gif ..\..\..\..\de.bmw.emf.evolution.declaration.edit\icons\full\obj16\Variable.gif

pause