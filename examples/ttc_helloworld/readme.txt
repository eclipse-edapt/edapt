The Edapt Solution for the Hello World Case
===========================================

Constituents of the Solution
----------------------------

The solution has the following directory structure:

- src: Java implementations of custom migrations
- description: contains the descriptions of the case and the solution
- material: contains the material that comes with the case
- solution: contains the solution; for each task there is a sub directory with
  * the metamodel(s) required for the transformation
  * the history model which specifies the transformation
  * a test model for the transformation
  * a launch configuration that applies the history model on the test model


Running the Solution
--------------------
  
To perform a transformation, you just need to right-click on the launch configuration 
and select "Run As -> <LaunchConfigName>" in the context menu. Alternatively you
can select "Run -> Run Configurations ..." in the menu and select the appropriate
launch configuration.


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
- Custom migrations can be inspected by selecting "Edapt->Edit Custom Migration"
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
