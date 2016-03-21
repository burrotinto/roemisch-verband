package com.ess.heuristiken.nextPosHeuristiken;

import java.awt.Point;

import com.ess.entity.IFlaecheK;

/**
 * Sucht die nächste freie Stelle, geht davon aus das es leichter ist die
 * Kürzere Seite zu verlegen. Passt sich aber an die Gegebenheiten an.
 * 
 * Hat sich aber herausgestellt das diese Idee länger braucht, da es hier einen
 * kritischen Punkt gibt der schwer zu verlegen ist.
 * 
 * @author Florian Klinger
 *
 */
public class NaechsteFreieStelleIntelligent implements INaechsteFreieStelleHeuristik {
	private final INaechsteFreieStelleHeuristik s = new NaechsteFreieStelleSpalte();
	private final INaechsteFreieStelleHeuristik z = new NaechsteFreieStelleZeile();

	@Override
	public Point getNaechsteFreieStelle(IFlaecheK flaeche) {
		Point spalte = s.getNaechsteFreieStelle(flaeche);
		Point zeile = z.getNaechsteFreieStelle(flaeche);
		if (spalte == zeile || spalte.equals(zeile)) {
			return spalte;
		}
		if (flaeche.getLaenge() - spalte.x > flaeche.getHoehe() - zeile.y) {
			return spalte;
		} else {
			return zeile;
		}
	}

}
