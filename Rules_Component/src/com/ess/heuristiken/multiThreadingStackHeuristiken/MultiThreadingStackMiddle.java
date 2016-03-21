package com.ess.heuristiken.multiThreadingStackHeuristiken;

import java.util.LinkedList;

import com.ess.entity.IFlaecheK;

/**
 * Entnimmt die Liste aus der mitte
 * 
 * @author Florian Klinger
 *
 */
public class MultiThreadingStackMiddle implements IMultiThreadingStackHeuristik {

	@Override
	public LinkedList<IFlaecheK> gibDeineHeuristischErmittelteListe(LinkedList<LinkedList<IFlaecheK>> stackListe) {
		return stackListe.remove(stackListe.size() / 2);
	}

}
