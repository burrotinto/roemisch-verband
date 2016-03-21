package com.ess.heuristiken.multiThreadingStackHeuristiken;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.ess.entity.IFlaecheK;

/**
 * Wählt die Subliste mit dem am Vollständigsten gelößten Teilfläche
 * 
 * @author Florian Klinger
 *
 */
public class MultiThreadingStackNextToFull implements IMultiThreadingStackHeuristik {

	@Override
	public LinkedList<IFlaecheK> gibDeineHeuristischErmittelteListe(LinkedList<LinkedList<IFlaecheK>> stackListe) {
		Collections.sort(stackListe,  new Comparator<LinkedList<IFlaecheK>>(){

			@Override
			public int compare(LinkedList<IFlaecheK> o1, LinkedList<IFlaecheK> o2) {
				return o1.getLast().getFuellstand() - o2.getLast().getFuellstand() ;
			}
			
		});

		return stackListe.removeLast();
	}

}
