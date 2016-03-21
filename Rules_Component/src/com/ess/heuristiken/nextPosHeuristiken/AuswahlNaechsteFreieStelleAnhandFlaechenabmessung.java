package com.ess.heuristiken.nextPosHeuristiken;

import java.awt.Point;

import com.ess.entity.IFlaecheK;

/**
 * Aswahl der naechsten freien Stelle anhand der Flaechenabmessung bedeutet, ist
 * die Flaeche laenger als hoch wird {@link NaechsteFreieStelleSpalte} benutzt,
 * ansonsten das Standart Verfahren innerhalb der Flaechenkontoller
 * 
 * @author Florian Klinger
 *
 */
public class AuswahlNaechsteFreieStelleAnhandFlaechenabmessung implements INaechsteFreieStelleHeuristik {

	@Override
	public Point getNaechsteFreieStelle(IFlaecheK flaeche) {
		if (flaeche.getHoehe() < flaeche.getLaenge()) {
			return new NaechsteFreieStelleSpalte().getNaechsteFreieStelle(flaeche);
		} else {
			return flaeche.getNaechsteFreieStelle();
		}
	}

}
