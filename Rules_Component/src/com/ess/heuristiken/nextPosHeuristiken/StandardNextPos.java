package com.ess.heuristiken.nextPosHeuristiken;

import java.awt.Point;

import com.ess.entity.IFlaecheK;

/**
 * Nimmt als Verfahren den effektiven vom {@link IFlaecheK} implementierten
 * Algorithmus zur findung der n‰chsten freien Stelle. ISt Ein Zeilenweiﬂes
 * Verfahren
 * 
 * @author Florian Klinger
 *
 */
public class StandardNextPos implements INaechsteFreieStelleHeuristik {

	@Override
	public Point getNaechsteFreieStelle(IFlaecheK flaeche) {
		return flaeche.getNaechsteFreieStelle();
	}

}
