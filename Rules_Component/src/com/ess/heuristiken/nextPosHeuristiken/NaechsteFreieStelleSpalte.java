package com.ess.heuristiken.nextPosHeuristiken;

import java.awt.Point;

import com.ess.entity.IFlaecheK;

/**
 * Diese Implementierung des Interfaces {@link INaechsteFreieStelleHeuristik}
 * gibt die nächste freie Stelle ausgehend der Spalten zurück
 * 
 * @author Florian Klinger
 *
 */
public class NaechsteFreieStelleSpalte implements INaechsteFreieStelleHeuristik {

	@Override
	public Point getNaechsteFreieStelle(IFlaecheK flaeche) {
		int x = 0;
		int y = 0;
		while (x < flaeche.getAnzahlQuadrateX()) {
			while (y < flaeche.getAnzahlQuadrateY()) {
				if (flaeche.getPlatteOnQuadrant(x, y) == null) {
					return new Point(x * flaeche.getQuadrantenLaenge(), y * flaeche.getQuadrantenLaenge());
				}
				y++;
			}
			y = 0;
			x++;
		}
		return null;
	}

}
