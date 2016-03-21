package com.ess.heuristiken.nextPosHeuristiken;

import java.awt.Point;

import com.ess.entity.IFlaecheK;

/**
 * Heuristik zur Auswahl der naechsten Freien Stelle
 * @author Florian Klinger
 *
 */
public interface INaechsteFreieStelleHeuristik {
	/**
	 * Gibt die n�chste freie Stelle zur�ck
	 * 
	 * @return Point einer freien stelle oder Null wenn die Flaeche voll ist
	 */
	Point getNaechsteFreieStelle(IFlaecheK flaeche);
}
