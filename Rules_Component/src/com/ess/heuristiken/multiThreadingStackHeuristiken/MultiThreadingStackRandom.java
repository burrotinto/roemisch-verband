package com.ess.heuristiken.multiThreadingStackHeuristiken;

import java.util.LinkedList;
import java.util.Random;

import com.ess.entity.IFlaecheK;

/**
 * Wählt per Zufall einen Eintrag aus der Liste
 * 
 * @author Florian Klinger
 *
 */
public class MultiThreadingStackRandom implements IMultiThreadingStackHeuristik {

	@Override
	public LinkedList<IFlaecheK> gibDeineHeuristischErmittelteListe(LinkedList<LinkedList<IFlaecheK>> stackListe) {

		return stackListe.remove(Math.abs(new Random().nextInt()) % stackListe.size());
	}

}
