package com.ess.heuristiken.multiThreadingStackHeuristiken;

import java.util.LinkedList;

import com.ess.entity.IFlaecheK;

/**
 * Implementiert den Stack im FIFO
 * 
 * @author Florian Klinger
 *
 */
public class MultiThreadingStackFIFO implements IMultiThreadingStackHeuristik {

	@Override
	public LinkedList<IFlaecheK> gibDeineHeuristischErmittelteListe(LinkedList<LinkedList<IFlaecheK>> stackListe) {
		return stackListe.pollFirst();
	}

}
