package com.ess.heuristiken;

import java.util.LinkedList;

import com.ess.entity.IFlaecheK;

/**
 * Zur ermittlung welche Liste als n�chstes angewand werden soll.
 * 
 * @author Florian Klinger
 *
 */
public interface IMultiThreadingHeuristik {

	/**
	 * Methode die die n�chste Liste an {@link IFlaecheK} zur�ckgibt. L�scht die
	 * zur�ckgegebene Liste aus der Parameterliste.
	 * 
	 * @param stackListe
	 *            Liste mit Liste von {@link IFlaecheK}
	 * @return die zu bearbeitende Liste
	 */
	LinkedList<IFlaecheK> ermittleMittelsHeuristik(LinkedList<LinkedList<IFlaecheK>> stackListe);

}
