package com.ess.heuristiken.multiThreadingStackHeuristiken;

import java.util.LinkedList;

import com.ess.entity.IFlaecheK;

/**
 * Zur ermittlung welche Liste als n�chstes angewand werden soll.
 * 
 * @author Florian Klinger
 *
 */
public interface IMultiThreadingStackHeuristik {

	/**
	 * Methode die die n�chste Liste an {@link IFlaecheK} zur�ckgibt. L�scht die
	 * zur�ckgegebene Liste aus der Parameterliste.
	 * 
	 * @param stackListe
	 *            Liste mit Liste von {@link IFlaecheK}
	 * @return die zu bearbeitende Liste
	 */
	LinkedList<IFlaecheK> gibDeineHeuristischErmittelteListe(LinkedList<LinkedList<IFlaecheK>> stackListe);

}
