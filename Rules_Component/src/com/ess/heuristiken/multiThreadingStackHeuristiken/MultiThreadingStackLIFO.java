package com.ess.heuristiken.multiThreadingStackHeuristiken;

import java.util.LinkedList;

import com.ess.entity.IFlaecheK;
/**
 * Implementiert den Stack im LIFO
 * 
 * @author Florian Klinger
 *
 */
public class MultiThreadingStackLIFO implements IMultiThreadingStackHeuristik {

	@Override
	public LinkedList<IFlaecheK> gibDeineHeuristischErmittelteListe(LinkedList<LinkedList<IFlaecheK>> stackListe) {
		return stackListe.pollLast();
	}

}
