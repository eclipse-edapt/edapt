/* --------------------------------------------------------------------------------
 * revision 1.193
 * date: 2007-01-03 17:32:28 +0000;  author: dstadnik;  lines: +6 -6;
 * enum literals should be uppercase to match constants in java interfaces
 * -------------------------------------------------------------------------------- */

// enum RulerUnits
rename(gmfgen.RulerUnits.Inches, "INCHES")
rename(gmfgen.RulerUnits.Centimeters, "CENTIMETERS")
rename(gmfgen.RulerUnits.Pixels, "PIXELS")

// enum Routing
rename(gmfgen.Routing.Manual, "MANUAL")
rename(gmfgen.Routing.Rectilinear, "RECTILINEAR")
rename(gmfgen.Routing.Tree, "TREE")
