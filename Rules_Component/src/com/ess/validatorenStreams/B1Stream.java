package com.ess.validatorenStreams;

import java.awt.Point;
import java.util.List;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Implementation des {@link IStream} der alle FliesenTypen aus dem
 * stream filtert die die Fugenlaengenregel verletzen wuerden
 * 
 * @author Florian Klinger
 *
 */
public class B1Stream extends AbstractFliesenStream {

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
	public B1Stream(IStream<IFliesenTyp> vorGaenger, List<IFliesenTyp> listeAllerFliesen, IFlaecheK flaeche, Point p, int fugenLaenge) {
		super(vorGaenger, listeAllerFliesen, flaeche, p, fugenLaenge);
	}

	@Override
	protected boolean istErlaubt(IFliesenTyp fliese) {
		return (testeKreuz2(getFlaeche(), fliese, ((getEinfuegePunkt().x + fliese.getX()) / getFlaeche().getQuadrantenLaenge()),
				((getEinfuegePunkt().y + fliese.getY()) / getFlaeche().getQuadrantenLaenge()), getFugenLaenge()) && testeKreuz(getFlaeche(), fliese, getEinfuegePunkt().x
				/ getFlaeche().getQuadrantenLaenge(), getEinfuegePunkt().y / getFlaeche().getQuadrantenLaenge(), getFugenLaenge()));
	}

	private boolean testeKreuz(IFlaecheK flaeche, IFliesenTyp iFliesenTyp, int x, int y, int maxFugenlaenge) {
		int fl = iFliesenTyp.getY();
		int y1 = y - 1;
		// Fuge nach oben testen
		if (x > 0 && x < flaeche.getAnzahlQuadrateX() - 1) {
			while (flaeche.getPlatteOnQuadrant(x, y1) != flaeche.getPlatteOnQuadrant(x - 1, y1)) {
				fl += flaeche.getQuadrantenLaenge();
				if (fl > maxFugenlaenge)
					return false;
				y1--;
			}

			// Fuge nach unten testen
			y1 = y + (iFliesenTyp.getY() / flaeche.getQuadrantenLaenge());
			while (flaeche.getPlatteOnQuadrant(x, y1) != flaeche.getPlatteOnQuadrant(x - 1, y1)) {
				fl += flaeche.getQuadrantenLaenge();
				if (fl > maxFugenlaenge)
					return false;
				y1++;
			}
		}

		if (y > 0 && y < flaeche.getAnzahlQuadrateY() - 1) {
			fl = iFliesenTyp.getX();
			int x1 = x - 1;
			// Fuge nach links testen
			while (flaeche.getPlatteOnQuadrant(x1, y) != flaeche.getPlatteOnQuadrant(x1, y - 1)) {
				fl += flaeche.getQuadrantenLaenge();
				if (fl > maxFugenlaenge)
					return false;
				x1--;
			}
			x1 = x + (iFliesenTyp.getX() / flaeche.getQuadrantenLaenge());
			// Fuge nach rechts testen
			while (flaeche.getPlatteOnQuadrant(x1, y) != flaeche.getPlatteOnQuadrant(x1, y - 1)) {
				fl += flaeche.getQuadrantenLaenge();
				if (fl > maxFugenlaenge)
					return false;
				x1++;
			}
		}
		return true;
	}

	private boolean testeKreuz2(IFlaecheK flaeche, IFliesenTyp iFliesenTyp, int x, int y, int maxFugenlaenge) {
		int fl = iFliesenTyp.getY();
		int y1 = y - 1 - (iFliesenTyp.getY() / flaeche.getQuadrantenLaenge());
		// Fuge nach oben testen
		if (x > 0 && x < flaeche.getAnzahlQuadrateX() - 1) {
			while (flaeche.getPlatteOnQuadrant(x, y1) != flaeche.getPlatteOnQuadrant(x - 1, y1)) {
				fl += flaeche.getQuadrantenLaenge();
				if (fl > maxFugenlaenge)
					return false;
				y1--;
			}

			// Fuge nach unten testen
			y1 = y;
			while (flaeche.getPlatteOnQuadrant(x, y1) != flaeche.getPlatteOnQuadrant(x - 1, y1)) {
				fl += flaeche.getQuadrantenLaenge();
				if (fl > maxFugenlaenge)
					return false;
				y1++;
			}
		}

		if (y > 0 && y < flaeche.getAnzahlQuadrateY() - 1) {
			fl = iFliesenTyp.getX();
			int x1 = x - 1 - (iFliesenTyp.getX() / flaeche.getQuadrantenLaenge());
			// Fuge nach links testen
			while (flaeche.getPlatteOnQuadrant(x1, y) != flaeche.getPlatteOnQuadrant(x1, y - 1)) {
				fl += flaeche.getQuadrantenLaenge();
				if (fl > maxFugenlaenge)
					return false;
				x1--;
			}
			x1 = x;
			// Fuge nach rechts testen
			while (flaeche.getPlatteOnQuadrant(x1, y) != flaeche.getPlatteOnQuadrant(x1, y - 1)) {
				fl += flaeche.getQuadrantenLaenge();
				if (fl > maxFugenlaenge)
					return false;
				x1++;
			}
		}
		return true;
	}
}
