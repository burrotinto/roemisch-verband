package com.ess.heuristiken.sortierHeuristiken;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Wie der Name schon sagt wird die Liste gemischt
 * @author Florian Klinger
 *
 */
public class RandomHeuristik implements ISortierHeuristik {

	@Override
	public List<IFliesenTyp> sortiereMittelsHeuristik(List<IFliesenTyp> fliesenTypen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge) {
		LinkedList<IFliesenTyp> list = new LinkedList<>(fliesenTypen);
		Collections.shuffle(list);
		return list;
	}
}
