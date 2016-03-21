package com.ess.validatorenStreams;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;
/**
 * Implementation des {@link IStream} der alle FliesenTypen aus dem
 * stream filtert die Fugenkreuze verursachen wuerden
 * 
 * @author Florian Klinger
 *
 */
public class B2Stream extends AbstractFliesenStream {
	/**
	 * Erzeugt den Stream
	 * 
	 * @param vorGaenger
	 *            Der {@link IStream} der diesen Stream speisst
	 * @param listeAllerFliesen
	 *            Liste aller erlaubten Fliesen Typen
	 * @param flaeche
	 *            Die zu Loesende teilverlegte Flaeche
	 * @param p
	 *            die Position wo die Fliesen eingefuegt werden sollen
	 * @param fugenLaenge
	 *            Maximal zulaessig
	 */
	public B2Stream(IStream<IFliesenTyp> vorGaenger, List<IFliesenTyp> listeAllerFliesen, IFlaecheK flaeche, Point p, int fugenLaenge) {
		super(vorGaenger, listeAllerFliesen, flaeche, p, fugenLaenge);

	}

	@Override
	protected boolean istErlaubt(IFliesenTyp fliese) {
		// Teste die relevanten Seiten
		// zuerst die rechte Oben
		if (checkQuadrantFuerFilterRechtsOben(getFlaeche(), ((getEinfuegePunkt().x + fliese.getX()) / getFlaeche().getQuadrantenLaenge()) - 1, (getEinfuegePunkt().y)
				/ getFlaeche().getQuadrantenLaenge())) {
			// Dann die ecke unten links
			if (checkQuadrantFuerFilterLinksUnten(getFlaeche(), getEinfuegePunkt().x / getFlaeche().getQuadrantenLaenge(), ((getEinfuegePunkt().y + fliese.getY()) / getFlaeche()
					.getQuadrantenLaenge()) - 1)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkQuadrantFuerFilterRechtsOben(IFlaecheK flaeche, int i, int j) {
		return !(flaeche.getPlatteOnQuadrant(i, j - 1) != flaeche.getPlatteOnQuadrant(i + 1, j - 1) && flaeche.getPlatteOnQuadrant(i + 1, j) != flaeche.getPlatteOnQuadrant(i + 1,
				j - 1));
	}

	private boolean checkQuadrantFuerFilterLinksUnten(IFlaecheK flaeche, int i, int j) {
		return !(flaeche.getPlatteOnQuadrant(i - 1, j) != flaeche.getPlatteOnQuadrant(i - 1, j + 1) && flaeche.getPlatteOnQuadrant(i - 1, j + 1) != flaeche.getPlatteOnQuadrant(i,
				j + 1));
	}
}
