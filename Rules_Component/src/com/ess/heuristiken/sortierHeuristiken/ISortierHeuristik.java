package com.ess.heuristiken.sortierHeuristiken;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Klassen die dieses Interface implementieren sind in der Lage aussagen über
 * die FliesenTypen zu machen die an einem bestimmten Punkt der Fläche verlegt
 * werden sollen.
 * 
 * @author Florian Klinger
 *
 */
public interface ISortierHeuristik {

	/**
	 * Sortier die Liste der Fliesen anhand einer Heuristik
	 * 
	 * @param fliesenTypen
	 *            die zur auswah stehen
	 * @param flache
	 *            in der verlegt wird
	 * @param einfuegePunkt
	 *            wo eingefuegt werden soll
	 * @param fugenlaenge
	 *            maxial erlaubt
	 * @return die Sortierte Liste
	 */
	List<IFliesenTyp> sortiereMittelsHeuristik(List<IFliesenTyp> fliesenTypen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge);

}
