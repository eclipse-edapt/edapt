The Edapt Solution for the GMF Model Migration Case
===================================================

Constituents of the Solution
----------------------------

The solution has the following directory structure:

- description: contains the descriptions of the case and the solution
- models: contains the test models that come with the case
- solution: contains the solutions to all the tasks
  * the metamodels required for the migrations
  * the history models which specify the migrations
  * launch configurations that apply the history model on the test models of 
    the GMF Graph metamodel


Running the Solution
--------------------

There are different ways to execute the history models of both metamodels:

- GMF Graph: To perform the transformation, you just right-click on the launch 
  configuration and select "Run As -> <LaunchConfigName>" in the context menu. 
  Alternatively you can select "Run -> Run Configurations ..." in the menu and 
  select the appropriate launch configuration.
- GMF Map: You need to start a second runtime Eclipse workbench by choosing 
  "Run -> Eclipse". In the runtime Eclipse workbench, you can migrate a GMF
  Map model by selecting "Edapt -> Migrate Model" in the context menu. The 
  runtime Eclipse workbench is necessary to be able to resolve the other 
  metamodels to which the GMF Map metamodel refers.


Inspecting the Solution
-----------------------

The history model is best inspected by opening the corresponding ecore file. 
As explained in the solution paper, the history is shown below the current 
metamodel version.

The history model can be inspected using the following functions:
- The "Reconstruction View" provides a live reconstruction of the metamodel 
  version after the operation selected in the history model.
- The integrity of the history model can be checked by selecting
  "Edapt->Check Integrity" on the history element.
- Custom migration can be inspected by selecting "Edapt->Edit Custom Migration"
  in the context menu on the custom migration.
  
  
Changing the Solution
---------------------

The solution can be changed using the following functions:
- The "Operation Browser" can be used to execute additional reusable coupled 
  operations.
- Pressing "Converge" in the "Operation Browser" allows to select the target 
  metamodel version to which the currently open metamodel version should 
  converge.
- Operations can be reordered by simply dragging them.
- Selecting "Edapt->Undo Changes" on an operation in the context menu allows to 
  undo the operation.
- Selecting "Edapt->Replace primitive changes with operation" on a sequence of 
  primitive changes opens a dialog to select the reusable coupled operation by 
  which the primitive changes need to be replaced.
- Reusable and custom coupled operations can be reduced to the corresponding 
  primitive metamodel changes by selecting "Edapt->Flatten Composite".
- A custom coupled operation can be introduced by selecting
  "Edapt->Attach Custom Migration to Changes" on the metamodel changes to 
  which a custom migration should be attached.


Thanks for trying out Edapt.
If you have problems, do not hesitate to contact me (herrmama@in.tum.de).
