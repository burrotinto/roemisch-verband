package com.ess.heuristiken.nextPosHeuristiken;

import java.awt.Point;

import com.ess.entity.IFlaecheK;

/**
 * Diese Implementierung des Interfaces {@link INaechsteFreieStelleHeuristik}
 * gibt die n�chste freie Stelle ausgehend der Zeilen zur�ck.
 * 
 * @author Florian Klinger
 *
 */
public class NaechsteFreieStelleZeile implements INaechsteFreieStelleHeuristik {
	private int xLast = 0;
	private int yLast = 0;

	@Override
	public Point getNaechsteFreieStelle(IFlaecheK flaeche) {
		// Mit dem Wissen �ber die Existenz des MuliThreadL�ser, ist diese
		// Abfrage
		// wichtig
		if (flaeche.getPlatteOnQuadrant(xLast, yLast) == null) {
			xLast = 0;
			yLast = 0;
		}
		int x = xLast;
		int y = yLast;
		while (y < flaeche.getAnzahlQuadrateY()) {
			while (x < flaeche.getAnzahlQuadrateX()) {
				if (flaeche.getPlatteOnQuadrant(x, y) == null) {
					return new Point(x * flaeche.getQuadrantenLaenge(), y * flaeche.getQuadrantenLaenge());
				}
				x++;
			}
			x = 0;
			y++;
		}
		return null;
	}

}
