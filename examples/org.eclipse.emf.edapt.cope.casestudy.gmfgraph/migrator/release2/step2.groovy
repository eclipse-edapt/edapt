/* --------------------------------------------------------------------------------
 * revision 1.32
 * date: 2008-04-18 14:43:28 +0000;  author: atikhomirov;  lines: +1 -5;
 * with [221352] resolved, we are safe to use readonly backreferences again, without suppressSetVisibility hack
 * -------------------------------------------------------------------------------- */

// class ChildAccess
gmfgraph.ChildAccess.owner.changeable = false
gmfgraph.ChildAccess.owner.eAnnotations[0].delete()
// cancelled out by step 1
