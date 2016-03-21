package com.ess.validatoren;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ess.entity.IFlaecheK;
import com.ess.entity.IFliesenTyp;

/**
 * Implementierung der Bedingung B1 - Maximale Fugenlänge.
 * 
 * Ist gleichzeitig auch eine Filternde Heuristik.
 * 
 * @author Florian Klinger
 *
 */
public class B1MaximaleFugenlaenge extends AbstractBedingung {
	@Override
	public boolean pruefe(IFlaecheK flaeche, Map<String, IFliesenTyp> fliesenTypen, int maxFugenLaenge) {
		if (flaeche != null) {
			// jede Zeile und Spalte wird abgelaufen um die Fugenlänge zu
			// überprüfen
			for (int x = 1; x < flaeche.getAnzahlQuadrateX(); x++) {
				// Dabei wird geschaut ob an beiden Seiten der Fuge die gleiche
				// Platte liegt, tu sie dass endet dort die Fuge.
				if (!checkFugeSenkrecht(flaeche, maxFugenLaenge, x)) {
					return false;
				}
			}
			for (int y = 1; y < flaeche.getAnzahlQuadrateY(); y++) {
				if (!checkFugeWaagrecht(flaeche, maxFugenLaenge, y)) {
					return false;
				}
			}
		}
		return true;
	}

	protected boolean checkFugeWaagrecht(IFlaecheK flaeche, int maxFugenLaenge, int y) {
		if (y > 0 && y < flaeche.getAnzahlQuadrateY() - 1) {
			int aktuelleLaengeDerFuge = 0;
			for (int x = 0; x < flaeche.getAnzahlQuadrateX(); x++) {
				if (flaeche.getPlatteOnQuadrant(x, y) == flaeche.getPlatteOnQuadrant(x, y - 1)) {
					aktuelleLaengeDerFuge = 0;
				} else {
					aktuelleLaengeDerFuge += flaeche.getQuadrantenLaenge();
					if (aktuelleLaengeDerFuge > maxFugenLaenge) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected boolean checkFugeSenkrecht(IFlaecheK flaeche, int maxFugenLaenge, int x) {
		if (x > 0 && x < flaeche.getAnzahlQuadrateX() - 1) {
			int aktuelleLaengeDerFuge = 0;
			for (int y = 0; y < flaeche.getAnzahlQuadrateY(); y++) {
				if (flaeche.getPlatteOnQuadrant(x, y) == flaeche.getPlatteOnQuadrant(x - 1, y)) {
					aktuelleLaengeDerFuge = 0;
				} else {
					aktuelleLaengeDerFuge += flaeche.getQuadrantenLaenge();
					if (aktuelleLaengeDerFuge > maxFugenLaenge) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public List<IFliesenTyp> filtereUnmoeglicheTypen(List<IFliesenTyp> fliesenTypen, IFlaecheK flache, Point einfuegePunkt, int fugenlaenge) {
		LinkedList<IFliesenTyp> liste = new LinkedList<>();
		for (IFliesenTyp iFliesenTyp : fliesenTypen) {
			if (testeKreuz2(flache, iFliesenTyp, ((einfuegePunkt.x + iFliesenTyp.getX()) / flache.getQuadrantenLaenge()),
							((einfuegePunkt.y + iFliesenTyp.getY()) / flache.getQuadrantenLaenge()), fugenlaenge) && testeKreuz(flache, iFliesenTyp, einfuegePunkt.x / flache.getQuadrantenLaenge(), einfuegePunkt.y / flache.getQuadrantenLaenge(), fugenlaenge)) {
				liste.add(iFliesenTyp);
			} 

		}
		return liste;
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

	@Override
	public int aufrufReihenfolge() {
		return 2;
	}

}
