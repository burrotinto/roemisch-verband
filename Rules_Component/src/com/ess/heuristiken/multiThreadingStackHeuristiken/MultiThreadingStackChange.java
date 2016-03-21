package com.ess.heuristiken.multiThreadingStackHeuristiken;

import java.util.LinkedList;

import com.ess.entity.IFlaecheK;

/**
 * Dieser "Stapel"- Verarbeitung nimmt immer vorn und hinten im wechsel vom
 * Ablagestapel
 * 
 * @author Florian Klinger
 *
 */
public class MultiThreadingStackChange implements IMultiThreadingStackHeuristik {
	private int next = 0;
	private IMultiThreadingStackHeuristik[] heus = { new MultiThreadingStackFIFO(), new MultiThreadingStackMiddle(), new MultiThreadingStackLIFO(), new MultiThreadingStackRandom(),new MultiThreadingStackNextToFull() };

	@Override
	public LinkedList<IFlaecheK> gibDeineHeuristischErmittelteListe(LinkedList<LinkedList<IFlaecheK>> stackListe) {
		LinkedList<IFlaecheK> list = heus[next].gibDeineHeuristischErmittelteListe(stackListe);
		next = (next + 1) % heus.length;
		return list;
	}

}
