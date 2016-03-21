package com.ess.validatoren;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
import com.ess.entity.IPlatte;

/**
 * Implementierung der Bedingung B2 - Keine Fugenkreuze.
 * 
 * Ist gleichzeitig auch eine Filternde Heuristik.
 * 
 * @author Florian Klinger
 *
 */
public class B2KeineFugenkreuze extends AbstractBedingung {

	@Override
	public boolean pruefe(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge) {
		if (flaeche != null) {
			for (int i = 1; i < flaeche.getAnzahlQuadrateX(); i++) {
				for (int j = 1; j < flaeche.getAnzahlQuadrateY(); j++) {
					// Wenn alle nachbarn unterschiedlich sind ist der punkt
					// tatsächlich ein kreuz
					if (!checkQuadrant(flaeche, i, j)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean checkQuadrant(IFlaecheK flaeche, int i, int j){
		IPlatte[] kreuz = { flaeche.getPlatteOnQuadrant(i, j), flaeche.getPlatteOnQuadrant(i - 1, j), flaeche.getPlatteOnQuadrant(i - 1, j - 1),
				flaeche.getPlatteOnQuadrant(i, j - 1) };
		// Wenn alle nachbarn unterschiedlich sind ist der punkt tatsächlich
		// ein
		// kreuz
		if (kreuz[0] != kreuz[1] && kreuz[1] != kreuz[2] && kreuz[2] != kreuz[3] && kreuz[3] != kreuz[0]) {
			return false;
		}
		return true;
	}

	@Override
	public List<IFliesenTyp> filtereUnmoeglicheTypen(List<IFliesenTyp> fliesen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge){
		LinkedList<IFliesenTyp> list = new LinkedList<IFliesenTyp>();
		for (IFliesenTyp iFliesenTyp : fliesen) {
			// Teste die relevanten Seiten
			// zuerst die rechte Oben
			if (checkQuadrantFuerFilterRechtsOben(flache, ((einfuegePunkt.x + iFliesenTyp.getX()) / flache.getQuadrantenLaenge()) - 1,
					(einfuegePunkt.y) / flache.getQuadrantenLaenge()))
				// Dann die ecke unten links
				if (checkQuadrantFuerFilterLinksUnten(flache, einfuegePunkt.x / flache.getQuadrantenLaenge(),
						((einfuegePunkt.y + iFliesenTyp.getY()) / flache.getQuadrantenLaenge()) - 1))
					list.add(iFliesenTyp);
		}
		return list;
	}

	private boolean checkQuadrantFuerFilterRechtsOben(IFlaecheK flaeche, int i, int j){
		return !(flaeche.getPlatteOnQuadrant(i, j - 1) != flaeche.getPlatteOnQuadrant(i + 1, j - 1) && flaeche.getPlatteOnQuadrant(i + 1, j) != flaeche.getPlatteOnQuadrant(i + 1,
				j - 1));
	}

	private boolean checkQuadrantFuerFilterLinksUnten(IFlaecheK flaeche, int i, int j){
		return !(flaeche.getPlatteOnQuadrant(i - 1, j) != flaeche.getPlatteOnQuadrant(i - 1, j + 1) && flaeche.getPlatteOnQuadrant(i - 1, j + 1) != flaeche.getPlatteOnQuadrant(i,
				j + 1));
	}

	@Override
	public int aufrufReihenfolge() {
		return 0;	
	}

}
