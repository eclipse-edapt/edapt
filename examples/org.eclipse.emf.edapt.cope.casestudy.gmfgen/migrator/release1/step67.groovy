/* --------------------------------------------------------------------------------
 * revision 1.206
 * date: 2007-01-29 18:09:13 +0000;  author: ashatalin;  lines: +2 -0;
 * [167466] - Do not generate ??ItemSemanticEditPolicy for pure-design elements
 * -------------------------------------------------------------------------------- */

// class GenCommonBase
gmfgen.GenCommonBase.newEAttribute("sansDomain", emf.EBoolean, 0, 1)
gmfgen.GenCommonBase.sansDomain.changeable = false
gmfgen.GenCommonBase.sansDomain.'volatile' = true
gmfgen.GenCommonBase.sansDomain.'transient' = true
gmfgen.GenCommonBase.sansDomain.derived = true
