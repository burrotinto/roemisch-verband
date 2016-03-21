package com.ess.heuristiken.sortierHeuristiken;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Sortiert die {@link IFliesenTyp} anhand ihrer größe
 * 
 * @author Florian Klinger
 *
 */
public class GroessteZuErst implements ISortierHeuristik {

	@Override
	public List<IFliesenTyp> sortiereMittelsHeuristik(
			List<IFliesenTyp> fliesen, IFlaecheK flache,
			Point einfuegePunkt, int fugenlaenge) {
		LinkedList<IFliesenTyp> liste = new LinkedList<IFliesenTyp>(fliesen);

		Collections.sort(liste, new Comparator<IFliesenTyp>() {

			@Override
			public int compare(IFliesenTyp o1, IFliesenTyp o2) {
				return (-o1.getX()*o1.getY()) + (o2.getX()*o2.getY());
			}
		});
		return liste;
		
	}


}
